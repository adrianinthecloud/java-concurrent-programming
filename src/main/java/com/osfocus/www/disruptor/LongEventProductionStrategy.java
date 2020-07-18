package com.osfocus.www.disruptor;

import java.util.concurrent.BlockingQueue;

public class LongEventProductionStrategy implements IStrategy<LongEvent> {
    private long start;
    private long end;
    private LongEvent poisonPill;
    private int numOfPill;

    public LongEventProductionStrategy(long start, long end, LongEvent poisonPill, int numOfPill) {
        this.start = start;
        this.end = end;
        this.poisonPill = poisonPill;
        this.numOfPill = numOfPill;
    }

    @Override
    public void process(BlockingQueue<LongEvent> queue) {
        long index = start;
        while (index < end) {
            try {
                queue.put(new LongEvent(index++));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < numOfPill; i++) {
            try {
                queue.put(poisonPill);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
