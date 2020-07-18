package com.osfocus.www.disruptor;

import java.util.concurrent.BlockingQueue;

public class BlockingQueueProducer<E> implements Runnable {
    private BlockingQueue<E> blockingQueue;
    private IStrategy strategy;

    public BlockingQueueProducer(BlockingQueue<E> blockingQueue, IStrategy<E> productionStrategy) {
        this.blockingQueue = blockingQueue;
        this.strategy = productionStrategy;
    }

    @Override
    public void run() {
        process();
    }

    public void process() {
        strategy.process(blockingQueue);
    }
}
