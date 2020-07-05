package com.osfocus.www.basic.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {
    private static final int NUM_OF_READS = 5;
    private static final int NUM_OF_WRITES = 2;

    public static void doRead(Lock lock) {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " is doing read.");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void doWrite(Lock lock) {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " is doing write.");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(NUM_OF_READS + NUM_OF_WRITES);
        ReentrantLock lock = new ReentrantLock();

        for (int i = 0; i < NUM_OF_READS; i++) {
            new Thread(() -> {
                doRead(lock);
                latch.countDown();
            }).start();
        }

        for (int i = 0; i < NUM_OF_WRITES; i++) {
            new Thread(() -> {
                doWrite(lock);
                latch.countDown();
            }).start();
        }
        long start = System.currentTimeMillis();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("Single Lock took " + (end - start) + "ms.\n");

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock readLock = readWriteLock.readLock();
        Lock writeLock = readWriteLock.writeLock();

        CountDownLatch latch2 = new CountDownLatch(NUM_OF_READS + NUM_OF_WRITES);
        for (int i = 0; i < NUM_OF_READS; i++) {
            new Thread(() -> {
                doRead(readLock);
                latch2.countDown();
            }).start();
        }

        for (int i = 0; i < NUM_OF_WRITES; i++) {
            new Thread(() -> {
                doWrite(writeLock);
                latch2.countDown();
            }).start();
        }
        start = System.currentTimeMillis();

        try {
            latch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();

        System.out.println("ReadWrite Lock took " + (end - start) + "ms.");
    }
}
