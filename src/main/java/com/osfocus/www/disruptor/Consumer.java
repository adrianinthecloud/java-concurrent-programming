package com.osfocus.www.disruptor;

import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements WorkHandler<LongEvent> {
    public static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onEvent(LongEvent longEvent) throws Exception {
        count.getAndIncrement();
    }
}
