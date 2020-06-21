package com.osfocus.www.completable_future;

import java.util.concurrent.*;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * This solution is more elegant and has better structure.
 * Also the order process does not block the main thread,
 * which means we can get more work done with the same time spending on processing the order.
 */
public class CompletableFutureTravelBooking {
    private static void printOrder(String flightOrder, String hotelOrder, String carOder) {
        System.out.println("Order Information:");
        System.out.printf("Flight Number: %s\n", flightOrder);
        System.out.printf("Hotel: %s\n", hotelOrder);
        System.out.printf("Car: %s\n", carOder);

    }
    
    public static void main(String args[]) {
        // In real world practise, we use customized policy for RejectedExecutionHandler
        // such as saving the message to Kafka, redis, database or make a log message for it.
        ExecutorService executor = new ThreadPoolExecutor(3, 10,
                60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10),
                new TravelBookingThreadFactory("Travel-Booking"),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        // By default, CompletableFuture internally uses ForkJoinPool.commonPool
        // If a running server is two cores server, the pool will have just 1 thread to execute tasks.
        // This may lead to unacceptable response times
        // if the thread is working on a task that takes long time to finish
        // That's why we use our customized executor
        CompletableFuture<String> flightFuture = CompletableFuture.supplyAsync(() -> {
            // dummy flight booking process
            System.out.println(Thread.currentThread().getName() + " is searching for available flights");
            // process some business logic for 2 seconds
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "CZ325";
        }, executor);

        CompletableFuture<String> hotelFuture = CompletableFuture.supplyAsync(() -> {
            // dummy hotel booking process
            System.out.println(Thread.currentThread().getName() + " is searching for available hotels");
            // process some business logic for 2 seconds
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hilton";
        }, executor);

        CompletableFuture<String> carFuture = CompletableFuture.supplyAsync(() -> {
            // dummy car booking process
            System.out.println(Thread.currentThread().getName() + " is searching for available cars");
            // process some business logic for 2 seconds
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "BMW M850i";
        }, executor);

        CompletionStage<Void> allFuture = CompletableFuture.allOf(flightFuture, hotelFuture, carFuture);
        allFuture.thenAccept(voidData -> printOrder(flightFuture.join(), hotelFuture.join(), carFuture.join()));

        System.out.println("Main Thread is going to do other things");

        executor.shutdown();
    }
}
