package com.p6majo.thread;

import com.p6majo.info.aachen.Kon;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {

    static int counter = 0;
    static Timer timer;

    @Test
    public void start() {

        //create timer task to increment counter
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                System.out.println("TimerTask executing counter is: " + counter);
                counter++;
            }
        };

        //create thread to print counter value
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("Thread reading counter is: " + counter);
                        if (counter == 10) {
                            System.out.println("Counter has reached 3 now will terminate");
                            timer.cancel();//end the timer
                            break;//end this loop
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        timer = new Timer("MyTimer");//create a new timer
        timer.scheduleAtFixedRate(timerTask, 30, 3000);//start timer in 30ms to increment  counter

        t.start();//start thread to display counter
        Kon.readInt();
    }
}