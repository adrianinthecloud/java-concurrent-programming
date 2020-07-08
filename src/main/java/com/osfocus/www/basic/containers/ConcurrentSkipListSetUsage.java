package com.osfocus.www.basic.containers;

import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConcurrentSkipListSetUsage {
    public static void main(String[] args) {
        ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 20; j++) {
                    set.add(Integer.valueOf((int) (101 * Math.random())));
                }
            });
        }

        Arrays.asList(threads).forEach(t -> t.start());
        Arrays.asList(threads).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        int count = 0;
        for (Integer item : set) {
            System.out.print(item);
            count++;
            if (count < set.size()) {
                System.out.print("-");
            }
        }
    }
}
