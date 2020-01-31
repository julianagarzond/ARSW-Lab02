# ARSW-Lab02

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

