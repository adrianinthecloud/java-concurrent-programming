package com.osfocus.www.basic.containers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSetUsage {
    private static final int NUM_OF_THREAD = 10000;

    public static void main(String[] args) {
        Set<Integer> concurrentHashSet = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());
        Set<Integer> normalSet = new HashSet<>();

        Thread[] threads = new Thread[NUM_OF_THREAD];

        for (int i = 0; i < NUM_OF_THREAD; i++) {
            threads[i] = new Thread(() -> {
               for (int j = 0; j < 100; j++) {
                   int num = (int) (10001 * Math.random());
                   concurrentHashSet.add(num);
                   normalSet.add(num);
               }
            });
        }

        Arrays.asList(threads).forEach(t -> {
            t.start();
        });

        Arrays.asList(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("concurrentHashSet size = " + concurrentHashSet.size());
        System.out.println("normalSet size = " + normalSet.size());
    }
}
