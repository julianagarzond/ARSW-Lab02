package edu.eci.arsw.highlandersim;

import java.util.Random;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback = null;

    private int health;

    private AtomicBoolean isPaused;

    private int defaultDamageValue;

    private final  CopyOnWriteArrayList<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());


    public Immortal(String name, CopyOnWriteArrayList<Immortal>  immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue = defaultDamageValue;
        isPaused = new AtomicBoolean(false);
    }

    public void run() {

        while (true) {
            synchronized (isPaused){
                while (isPaused.get()){
                    try{
                        isPaused.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            synchronized (immortalsPopulation) {
                this.fight(im);
            }



            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) {

        if (i2.getHealth() > 0) {
            i2.changeHealth(i2.getHealth() - defaultDamageValue);
            this.health += defaultDamageValue;
            updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
        } else {
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
        }

    }

    public void changeHealth(int v) {
        if(this.health > 0) {
            health = v;
        }else{
            health = v;
            immortalsPopulation.remove(this);
        }
    }

    public int getHealth() {
        return health;
    }

    public void pause(){
        isPaused.set(!isPaused.get());
    }

    public void wake(){
        synchronized (isPaused) {
            isPaused.notify();
        }
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
