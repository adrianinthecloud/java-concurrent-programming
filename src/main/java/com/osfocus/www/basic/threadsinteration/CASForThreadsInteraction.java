package com.osfocus.www.basic.threadsinteration;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using AtomicBoolean which internally using compareAndSet to set value
 */
public class CASForThreadsInteraction {
    private static AtomicBoolean flag = new AtomicBoolean(true);
    static Thread t1, t2 = null;

    public static void main(String args[]) {
        t1 = new Thread(() -> {
            Character baseLetter = 'A';
            for (int i = 0; i < 7; i++) {
                while (!flag.get()) { }
                System.out.print((char) (baseLetter+i));
                flag.set(false);
            }
        });

        t2 = new Thread(() -> {
            for (int i = 1; i <= 7; i++) {
                while (flag.get()) { }
                System.out.print(i);
                flag.set(true);
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
