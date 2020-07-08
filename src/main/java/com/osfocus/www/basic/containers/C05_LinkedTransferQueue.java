package com.osfocus.www.basic.containers;

import java.util.concurrent.LinkedTransferQueue;

public class C05_LinkedTransferQueue {
    public static void main(String[] args) {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " took " + queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        queue.offer("aaa");
        try {
            // will block the thread.
            queue.transfer("bbb");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("End");
    }
}
