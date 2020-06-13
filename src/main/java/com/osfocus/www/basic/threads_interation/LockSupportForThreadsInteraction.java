package com.osfocus.www.basic.threads_interation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using LockSupport
 */
public class LockSupportForThreadsInteraction {
    static Thread t1, t2 = null;

    public static void main(String args[]) {


        t1 = new Thread(() -> {
            Character baseLetter = 'A';
            for (int i = 0; i < 7; i++) {
                System.out.print((char) (baseLetter+i));

                LockSupport.unpark(t2);
                LockSupport.park();
            }
        });

        t2 = new Thread(() -> {
            for (int i = 1; i <= 7; i++) {
                LockSupport.park();
                System.out.print(i);

                LockSupport.unpark(t1);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
