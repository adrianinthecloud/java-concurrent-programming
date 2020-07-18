package com.osfocus.www.disruptor;

import java.util.concurrent.CountDownLatch;

public class SimpleTimeCounter {
    private String feature;
    private volatile long start = -1L;
    private long end;
    private CountDownLatch latch;
    private int numToCount;
    private int numOfTasks;

    public SimpleTimeCounter(String feature, int numToCount, int numOfTasks) {
        this.feature = feature;
        this.latch = new CountDownLatch(numToCount);
        this.numToCount = numToCount;
        this.numOfTasks = numOfTasks;
    }

    public void start() {
        if (start == -1L) {
            synchronized (SimpleTimeCounter.class) {
                if (start == -1L) {
                    start = System.currentTimeMillis();
                }
            }
        }
    }

    public void count() {
        latch.countDown();
    }

    public boolean isCompleted() {
        return latch.getCount() == numToCount;
    }

    public void waitForTasksCompletion() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void timeConsumption() {
        end = System.currentTimeMillis();

        System.out.println(feature + " took " + (end - start) + "ms to handle " + numOfTasks + " tasks");
    }
}
