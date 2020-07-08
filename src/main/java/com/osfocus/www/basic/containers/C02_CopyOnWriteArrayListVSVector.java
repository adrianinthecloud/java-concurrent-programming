package com.osfocus.www.basic.containers;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

// insert performance comparison
public class C02_CopyOnWriteArrayListVSVector {
    private static final int NUM_OF_THREADS = 100;
    private static final int NUM_OF_ITEMS = 1000;

    static void runAndComputeTime(Thread[] ths) {
        long start = System.currentTimeMillis();
        Arrays.asList(ths).forEach(t->t.start());
        Arrays.asList(ths).forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("It took " + (end - start) + "ms");
    }

    public static void main(String[] args) {
        List<Integer> list = new CopyOnWriteArrayList<>();
        Vector<Integer> vector = new Vector<>();

        Thread[] threads = new Thread[NUM_OF_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_OF_ITEMS; j++) {
                    list.add((int) (1001 * Math.random()));
                }
            });
        }

        System.out.println("Performance of CopyOnWriteArrayList: ");
        runAndComputeTime(threads);

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                vector.add((int) (1001 * Math.random()));
            });
        }

        System.out.println("Performance of Vector: ");
        runAndComputeTime(threads);
    }
}
