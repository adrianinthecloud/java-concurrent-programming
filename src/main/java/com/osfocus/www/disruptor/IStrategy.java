package com.osfocus.www.disruptor;

import java.util.concurrent.BlockingQueue;

public interface IStrategy<E> {
    void process(BlockingQueue<E> queue);
}
