package com.osfocus.www.basic.threads_interation;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using synchronization on an object
 */
public class ObjectSynchronizationForThreadsInteraction {
    private static Thread t1, t2 = null;

    public static void main(String args[]) {
        Object obj = new Object();

        t1 = new Thread(() -> {
            char baseLetter = 'A';
            for (int i = 0; i < 7; i++) {
                synchronized (obj) {
                    System.out.print((char) (baseLetter+i));
                    obj.notify();
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t2 = new Thread(() -> {
            for (int i = 1; i <= 7; i++) {
                synchronized (obj) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print(i);
                    obj.notify();
                }
            }
        });

        t2.start();
        t1.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
