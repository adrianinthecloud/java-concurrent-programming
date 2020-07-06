package com.osfocus.www.threadpools;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class T03_ForkJoinPool {
    private static final int[] NUMS = new int[500000];
    private static final int MAX = 50000;

    static {
        for (int i = 0; i < NUMS.length; i++) {
            NUMS[i] = (int) (101 * Math.random());
        }
    }

    private static class AddTask extends RecursiveTask<Long> {
        private int start;
        private int end;

        public AddTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= MAX) {
                long count = 0;
                for (int i = start; i < end; i++) {
                    count += NUMS[i];
                }
                System.out.println(Thread.currentThread().getName() + " has result from " + start + " to " + end + " = " + count);
                return count;
            } else {
                int mid = start + ((end - start) >> 1);
                AddTask task1 = new AddTask(start, mid);
                AddTask task2 = new AddTask(mid, end);
                task1.fork();
                task2.fork();

                return task1.join() + task2.join();
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool fjp = new ForkJoinPool();

        AddTask task = new AddTask(0, NUMS.length);
        fjp.execute(task);

        System.out.println("Result = " + task.join());
    }
}
