package com.osfocus.www.disruptor;

import java.util.concurrent.CountDownLatch;

public class SimpleTimeCounter {
    private String feature;
    private long start;
    private long end;
    private CountDownLatch latch;
    private int numOfTasks;

    public SimpleTimeCounter(String feature, int numOfProducers, int numOfTasks) {
        this.feature = feature;
        this.latch = new CountDownLatch(numOfProducers);
        this.numOfTasks = numOfTasks;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void count() {
        latch.countDown();
    }

    public void waitForTasksCompletion() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();

        System.out.println(feature + " took " + (end - start) + "ms to handle " + numOfTasks + " tasks");
    }
}
