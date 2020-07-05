package com.osfocus.www.basic.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

// IllegalMonitorStateException is thrown when a thread is not the owner of a monitor and trying to notify() and notifyAll()
// or a thread tryRelease a lock when it is not exclusive owner thread.
// It will also be thrown when a condition try to notify which is supposed to be signal.
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

        ReentrantLock lock = new ReentrantLock();

        t1 = new Thread(() -> {
            try {
                lock.lock();
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + " got the lock.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " released the lock.");
            }
        }, "T1");

        t2 = new Thread(() -> {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " is going to release the lock.");
        }, "T2");

        t1.start();t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End Main.");
    }
}
