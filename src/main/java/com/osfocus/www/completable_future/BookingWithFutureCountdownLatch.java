package com.osfocus.www.completable_future;

import java.util.concurrent.*;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 */
public class BookingWithFutureCountdownLatch {
    public static void main(String args[]) {
        // in real world practise, we use customized policy for RejectedExecutionHandler
        // such as saving the message to Kafka, redis, database or make a log message for it.
        ExecutorService executorService = new ThreadPoolExecutor(3, 10,
                60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10),
                new TravelBookingThreadFactory("Travel-Booking"),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        CountDownLatch latch = new CountDownLatch(3);
        Future<String> flightFuture = executorService.submit(() -> {
            // dummy flight booking process
            System.out.println(Thread.currentThread().getName() + " is searching for available flights");
            // process some business logic for 2 seconds
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            return "CZ325";
        });

        Future<String> hotelFuture = executorService.submit(() -> {
            // dummy hotel booking process
            System.out.println(Thread.currentThread().getName() + " is searching for available hotels");
            // process some business logic for 2 seconds
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            return "Hilton";
        });

        Future<String> carFuture = executorService.submit(() -> {
            // dummy car booking process
            System.out.println(Thread.currentThread().getName() + " is searching for available cars");
            // process some business logic for 2 seconds
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            return "BMW M850i";
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Order Information:");
        try {
            System.out.printf("Flight Number: %s\n", flightFuture.get());
            System.out.printf("Hotel: %s\n", hotelFuture.get());
            System.out.printf("Car: %s\n", carFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
