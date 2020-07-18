package com.osfocus.www.disruptor;

import java.util.concurrent.BlockingQueue;

public class BlockingQueueConsumer<E> implements Runnable {
    private BlockingQueue<E> blockingQueue;
    private IStrategy strategy;

    public BlockingQueueConsumer(BlockingQueue<E> blockingQueue, IStrategy strategy) {
        this.blockingQueue = blockingQueue;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process() {
        strategy.process(blockingQueue);
    }
}
