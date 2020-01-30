/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hcadavid
 */
public class Consumer extends Thread {

    private Queue<Integer> queue;


    public Consumer(Queue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        //original();
        //optimized();
        lowConsumption();
    }


    public void original(){
        while (true) {

            if (queue.size() > 0) {
                int elem=queue.poll();
                System.out.println("Consumer consumes "+elem);
            }

        }
    }

    public void optimized(){
        while (true) {

            synchronized (queue) {

                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (queue.size() > 0) {
                    int elem = queue.poll();
                    System.out.println("Consumer consumes " + elem);
                }
            }
        }
    }

    public void lowConsumption(){
        while (true) {
            if (queue.size() > 0) {
                int elem = queue.poll();
                System.out.println("Consumer consumes " + elem);
                synchronized (queue){
                    queue.notify();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}


