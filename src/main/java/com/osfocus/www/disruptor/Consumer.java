package com.osfocus.www.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements WorkHandler<LongEvent> {
    public static AtomicInteger count = new AtomicInteger(0);
    private SimpleTimeCounter timeCounter;
    private final long NUM_OF_TASKS;

    public Consumer(SimpleTimeCounter timeCounter, long NUM_OF_TASKS) {
        this.timeCounter = timeCounter;
        this.NUM_OF_TASKS = NUM_OF_TASKS;
    }

    @Override
    public void onEvent(LongEvent longEvent) throws Exception {
        if (count.incrementAndGet() == NUM_OF_TASKS) {
            timeCounter.count();
        }
    }
}
