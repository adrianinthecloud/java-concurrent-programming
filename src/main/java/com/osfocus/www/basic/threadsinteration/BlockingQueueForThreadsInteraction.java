package com.osfocus.www.basic.threadsinteration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using BlockingQueue.
 */
public class BlockingQueueForThreadsInteraction {
    static BlockingQueue<String> q1 = new ArrayBlockingQueue<>(1);
    static BlockingQueue<String> q2 = new ArrayBlockingQueue<>(1);

    public static void main(String args[]) {
        new Thread(() -> {
            Character baseLetter = 'A';
            for (int i = 0; i < 7; i++) {
                System.out.print((char) (baseLetter+i));
                try {
                    q1.put("OK");
                    q2.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 1; i <= 7; i++) {
                try {
                    q1.take();
                    q2.put("OK");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print(i);
            }
        }).start();
    }
}
