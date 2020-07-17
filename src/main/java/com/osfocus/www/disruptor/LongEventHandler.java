package com.osfocus.www.disruptor;

import com.lmax.disruptor.EventHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class LongEventHandler implements EventHandler<LongEvent> {
    public static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        count.getAndIncrement();
        System.out.println(Thread.currentThread().getName() + " is dealing with " + longEvent.getValue());
    }
}
