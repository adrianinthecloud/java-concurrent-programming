package com.osfocus.www.basic.locks;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class AtomicVsSyncVsLongAdder {
    private static long count = 0L;
    private static AtomicLong atomicLong = new AtomicLong();
    private static LongAdder longAdder = new LongAdder();

    public static void main(String args[]) throws InterruptedException {
        Thread[] threads = new Thread[100];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    synchronized (AtomicVsSyncVsLongAdder.class) {
                        count++;
                    }
                }
            });
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        long expiredTime = System.currentTimeMillis();

        System.out.println("Synchronized took " + (expiredTime-startTime) + "ms.");

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    atomicLong.incrementAndGet();
                }
            });
        }

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        expiredTime = System.currentTimeMillis();

        System.out.println("AtomicLong took " + (expiredTime-startTime) + "ms.");

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    longAdder.increment();
                }
            });
        }

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        expiredTime = System.currentTimeMillis();

        System.out.println("LongAdder took " + (expiredTime-startTime) + "ms.");
    }
}
