package com.osfocus.www.singleton;

import java.util.concurrent.CountDownLatch;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: Double-checked locking with volatile
 * This is one of the perfect versions for Singleton Pattern
 * The volatile keyword ensures the visibility of INSTANCE between threads.
 * It also prevents instructions reordering.
 */
public class VolatileDCL {
    private volatile static VolatileDCL INSTANCE;

    private VolatileDCL() {}

    public static VolatileDCL getInstance() {
        // operate some business logic here.
        // We need this null-checked because in most case the instance has been initialized.
        // With this null-checked statement, the performance can be improved by around 40%.
        if (INSTANCE == null) {
            synchronized (VolatileDCL.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VolatileDCL();
                }
            }
        }

        return INSTANCE;
    }

    public static void main(String[] args) {
        int numOfThreads = 1000;
        CountDownLatch latch = new CountDownLatch(numOfThreads);

        for (int i = 0; i < numOfThreads; i++) {
            new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(VolatileDCL.getInstance().hashCode());
            }).start();
        }
    }
}
