package com.osfocus.www.disruptor;

import com.lmax.disruptor.EventHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class LongEventHandler implements EventHandler<LongEvent> {
    public static AtomicInteger count = new AtomicInteger(0);
    private final long NUM_OF_TASKS;
    private final SimpleTimeCounter counter;

    public LongEventHandler(long numOfTasks, SimpleTimeCounter counter) {
        this.NUM_OF_TASKS = numOfTasks;
        this.counter = counter;
    }

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        if (count.incrementAndGet() == NUM_OF_TASKS) {
            counter.count();
        }
    }
}
