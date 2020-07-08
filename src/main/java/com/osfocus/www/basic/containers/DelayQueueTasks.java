package com.osfocus.www.basic.containers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTasks {
    static BlockingQueue<DelayTask> tasks = new DelayQueue<>();

    static class DelayTask implements Delayed {
        String name;
        long runTime;

        public DelayTask(String name, long runTime) {
            this.name = name;
            this.runTime = runTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
                return -1;
            } else if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return "DelayTask{" +
                    "name='" + name + '\'' +
                    ", runTime=" + runTime +
                    '}';
        }
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();

        DelayTask t1 = new DelayTask("task1", System.currentTimeMillis() + 1000);
        DelayTask t2 = new DelayTask("task2", System.currentTimeMillis() + 3000);
        DelayTask t3 = new DelayTask("task3", System.currentTimeMillis() + 2500);
        DelayTask t4 = new DelayTask("task4", System.currentTimeMillis() + 1500);
        DelayTask t5 = new DelayTask("task5", System.currentTimeMillis() + 500);

        try {
            tasks.put(t1);
            tasks.put(t2);
            tasks.put(t3);
            tasks.put(t4);
            tasks.put(t5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; i++) {
            try {
                System.out.println(tasks.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
