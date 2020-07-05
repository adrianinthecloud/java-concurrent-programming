package com.osfocus.www.threadpools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T01_SingleThreadPool {
    public static void main(String[] args) {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 8; i++) {
            final int j = i;
            singleThreadPool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " " + j);
            }, "T-" + i);
        }

        singleThreadPool.shutdown();

        try {
            singleThreadPool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main end.");
    }
}
