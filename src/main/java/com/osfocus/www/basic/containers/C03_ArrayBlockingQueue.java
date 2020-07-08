package com.osfocus.www.basic.containers;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// discard the last outbound item if not available after the wait time.
public class C03_ArrayBlockingQueue {
    static BlockingQueue<String> strs = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                strs.put("a"+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            strs.offer("test", 1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(strs);
    }
}
