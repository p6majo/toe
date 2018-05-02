package com.p6majo.Runnable;


/**
 * Why should one use runnable for setting up threads?<br>
 *
 * 1) Java doesn't support multiple inheritance, which means you can only extend one class in Java so once you extended Thread class you lost your chance and can not extend or inherit another class in Java.
 * <br>
 * 2) In Object oriented programming extending a class generally means adding new functionality, modifying or improving behaviors. If we are not making any modification on Thread than use Runnable interface instead.
 * <br>
 * 3) Runnable interface represent a Task which can be executed by either plain Thread or Executors or any other means. so logical separation of Task as Runnable than Thread is good design decision.
 * <br>
 * 4) Separating task as Runnable means we can reuse the task and also has liberty to execute it from different means. since you can not restart a Thread once it completes. again Runnable vs Thread for task, Runnable is winner.
 * <br>
 * 5) Java designer recognizes this and that's why Executors accept Runnable as Task and they have worker thread which executes those task.
 * <br>
 * 6) Inheriting all Thread methods are additional overhead just for representing a Task which can can be done easily with Runnable.
 */
public class RunnableDemo implements Runnable {
    public Thread t;
    private String threadName;
    boolean suspended = false;

    RunnableDemo( String name) {
        threadName = name;
        System.out.println("Creating " +  threadName );
    }

    public void run() {
        System.out.println("Running " +  threadName );
        try {
            for(int i = 10; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);
                // Let the thread sleep for a while.
                Thread.sleep(300);
                synchronized(this) {
                    while(suspended) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    void suspend() {
        suspended = true;
    }

    synchronized void resume() {
        suspended = false;
        notify();
    }
}


