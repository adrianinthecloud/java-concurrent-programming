package com.osfocus.www.disruptor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class LongEventConsumptionStrategy implements IStrategy<LongEvent> {
    public static AtomicInteger count = new AtomicInteger(0);
    private LongEvent poisonPill;
    private SimpleTimeCounter counter;

    public LongEventConsumptionStrategy(LongEvent poisonPill, SimpleTimeCounter counter) {
        this.poisonPill = poisonPill;
        this.counter = counter;
    }

    @Override
    public void process(BlockingQueue<LongEvent> queue) {
        while (true) {
            try {
                LongEvent event = queue.take();
                if (event == poisonPill) break;

                count.getAndIncrement();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        counter.count();
    }
}
