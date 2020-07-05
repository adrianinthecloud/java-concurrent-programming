package com.osfocus.www.collections.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Producer {
    public static void main(String[] args) {
        BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
        PrintEvenNumberStrategy strategy = new PrintEvenNumberStrategy();
        Consumer consumer = new Consumer(queue, strategy);

        new Thread(consumer).start();

        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " generated number " + i);
            try {
                queue.put(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            queue.put("STOP");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
