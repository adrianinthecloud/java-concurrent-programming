package com.osfocus.www.basic.locks;

// IllegalMonitorStateException is thrown when a thread is not the owner of a monitor and trying to notify() and notifyAll().
public class IllegalMonitorStateTest {
    static Object obj = new Object();

    public static void main(String[] args) {
        Thread t1, t2;
        t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " started.");
            synchronized (obj) {
                System.out.println(Thread.currentThread().getName() + " is doing something.");
            }
            System.out.println(Thread.currentThread().getName() + " ended.");
        });

        t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " started.");
            obj.notify();
            System.out.println(Thread.currentThread().getName() + " ended.");
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
