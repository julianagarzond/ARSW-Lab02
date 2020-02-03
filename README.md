# ARSW-Lab02

## Members
  - Juliana Garzón Duque
  - Eduardo Ocampo Arellano 

## Immortal Case
The purpose of this laboratory is for the student to know and apply concepts of concurrent programming, as well as strategies that avoid career conditions

### Part I - Before finishing class
Thread control with wait/notify. Producer/consumer

1. Check the operation of the program and run it. While this occurs, run jVisualVM and check the CPU consumption of the corresponding process. Why is this consumption? Which is the responsible class? 
  - The CPU consumption is beacuse the consumer is always asking if the queue is empty , the most of the times the answer is false , and       this question can be made a lot of times.  
  - The responsible class is "StartProduction" which is the one who controlls the producer and the consumer.
  ![Pic1](https://user-images.githubusercontent.com/43153078/73459427-d8103780-4344-11ea-96ee-e8fe31a25bfc.png) 
  ![pic2](https://user-images.githubusercontent.com/43153078/73459588-232a4a80-4345-11ea-8643-92bea20d2a0c.png)

  

2. Make the necessary adjustments so that the solution uses the CPU more efficiently, taking into account that - for now - production is slow and consumption is fast. Verify with JVisualVM that the CPU consumption is reduced. 

To reduce the  CPU consumption we implement a wait() and notify() , In the producer class we sysnchronized the queue becaese is the resource that are sharing both threads, when the producer add something to the queue it notify that there´s something to consume , also the consumer is waitng for the producer to notify and when the queue size is different to 0 it starts to consumes

![image](https://user-images.githubusercontent.com/43153078/73499826-2813ec00-438f-11ea-8f17-ccff805662a0.png)

``` java
public void optimized() {
        while (true) {

            dataSeed = dataSeed + rand.nextInt(100);
            System.out.println("Producer added " + dataSeed);
            queue.add(dataSeed);

            synchronized (queue) {
                queue.notify();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

``` 
``` java
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
    
  ``` 

3. Make the producer now produce very fast, and the consumer consumes slow. Taking into account that the producer knows a Stock limit (how many elements he should have, at most in the queue), make that limit be respected. Review the API of the collection used as a queue to see how to ensure that this limit is not exceeded. Verify that, by setting a small limit for the 'stock', there is no high CPU consumption or errors.



``` java

 public void lowConsumption() {

        while (true) {

            synchronized (queue) {
                if (queue.size() > 40) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            dataSeed = dataSeed + rand.nextInt(100);
            System.out.println("Producer added " + dataSeed);
            queue.add(dataSeed);

        }

    }

```
``` java
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

```
### Part II
Synchronization and Dead-Locks.

1. Review the “highlander-simulator” program, provided in the edu.eci.arsw.highlandersim package. This is a game in which:
 - You have N immortal players. 
 - Each player knows the remaining N-1 player.
 - Each player permanently attacks some other immortal. The one who first attacks subtracts M life points from his opponent, and            increases his own life points by the same amount. 
 - The game could never have a single winner. Most likely, in the end there are only two left, fighting indefinitely by removing and        adding life points. 
2. Review the code and identify how the functionality indicated above was implemented. Given the intention of the game, an invariant       should be that the sum of the life points of all players is always the same (of course, in an instant of time in which a time increase   reduction operation is not in process ). For this case, for N players, what should this value be?
  
    Each player has 100 points of health , so if there are N players the result will be NX100
    
    ![vidas](https://user-images.githubusercontent.com/43153078/73597584-6dedc300-44fb-11ea-9534-392c9c46fa0d.PNG) 
  
  
  
  
  
3. Run the application and verify how the ‘pause and check’ option works. Is the invariant fulfilled.?
    The invariant is not fullfilled, probably all the thread are changing the same memory state that is the health of the inmortals.
    
4. A first hypothesis that the race condition for this function (pause and check) is presented is that the program consults the list        whose values ​​it will print, while other threads modify their values. To correct this, do whatever is necessary so that, before        printing the current results, all other threads are paused. Additionally, implement the ‘resume’ option.
    For the "pause and check" option we synchronized the "Inmortal population" List
   
    
5. Check the operation again (click the button many times). Is the invariant fulfilled or not ?.
    The invariant is fulfilled, everytime we click the button the value is 300 with N=0.
    
6. Identify possible critical regions in regards to the fight of the immortals. Implement a blocking strategy that avoids race              conditions. Remember that if you need to use two or more ‘locks’ simultaneously, you can use nested synchronized blocks:
    
    The fight is the most important critical region, becuase all threads access to the variable that is the health of every mortal.
   
7. After implementing your strategy, start running your program, and pay attention to whether it comes to a halt. If so, use the jps and    jstack programs to identify why the program stopped.

8. Consider a strategy to correct the problem identified above (you can review Chapter 15 of Java Concurrency in Practice again).

9. Once the problem is corrected, rectify that the program continues to function consistently when 100, 1000 or 10000 immortals are        executed. If in these large cases the invariant begins to be breached again, you must analyze what was done in step 4.

10. An annoying element for the simulation is that at a certain point in it there are few living 'immortals' making failed fights with     'immortals' already dead. It is necessary to suppress the immortal dead of the simulation as they die.

  10.1 Analyzing the simulation operation scheme, could this create a race condition? Implement the functionality, run the simulation          and see what problem arises when there are many 'immortals' in it. Write your conclusions about it in the file ANSWERS.txt.

  10.2 Correct the previous problem WITHOUT using synchronization, since making access to the shared list of immortals sequential would       .make simulation extremely slow.

13. To finish, implement the STOP option.

