package com.osfocus.www.basic.containers;

import java.util.concurrent.*;

public class FutureTaskUsage {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 888;
            }
        });

        try {
            System.out.println("Future result = " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        FutureTask<String> futureTask1 = new FutureTask<>(()->{
            System.out.println(Thread.currentThread().getName() + " is working on Future Task1.");
        }, "futureTask1");

        service.submit(futureTask1);

        FutureTask<Integer> futureTask2 = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + " is working on Future Task2.");
           return 1;
        });

        service.submit(futureTask2);

        try {
            System.out.println("FutureTask1 result = " + futureTask1.get());
            System.out.println("FutureTask2 result = " + futureTask2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        service.shutdownNow();
    }
}
