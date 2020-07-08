package com.osfocus.www.basic.containers;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class C01_TicketSellerVectorVSConcurrentLinkedQueue {
    private static final int NUM_OF_THREADS = 1000;
    static Vector<String> ticketsVector = new Vector<>();
    static ConcurrentLinkedQueue<String> ticketsQueue = new ConcurrentLinkedQueue<>();

    static {
        for (int i = 0; i < 100000; i++) {
            String ticket = "V-T-"+i;
            ticketsVector.add(ticket);
            ticketsQueue.add(ticket);
        }
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[NUM_OF_THREADS];

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            threads[i] = new Thread(() -> {
                synchronized (C01_TicketSellerVectorVSConcurrentLinkedQueue.class) {
                    while (ticketsVector.size() > 0) {
                        String ticket = ticketsVector.remove(0);
                        // do some ticket sell business logic here.
                    }
                }
            });
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("TicketVector took " + (end - start) + " ms.");

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            threads[i] = new Thread(() -> {
                while (true) {
                    String s = ticketsQueue.poll();
                    if (s == null) break;
                    // do some ticket sell business logic here.
                }
            });
        }

        start = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        end = System.currentTimeMillis();
        System.out.println("Ticket queue took " + (end - start) + " ms.");
    }
}
