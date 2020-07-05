package com.osfocus.www.collections.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private BlockingQueue<Object> blockingQueue;
    private IStrategy strategy;

    public Consumer(BlockingQueue<Object> blockingQueue, IStrategy strategy) {
        this.blockingQueue = blockingQueue;
        this.strategy = strategy;
    }

    public BlockingQueue<Object> getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue<Object> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started to run");
        try {
            while (true) {
                Object msg = blockingQueue.take();
                strategy.output(msg);
                if (msg.toString().equals("STOP")) break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
