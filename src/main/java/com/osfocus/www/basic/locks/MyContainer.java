package com.osfocus.www.basic.locks;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Container sample worked as producer and consumer.
public class MyContainer<T> {
    final private LinkedList<T> lists = new LinkedList<>();
    final private int MAX = 10;
    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public void put(T t) {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " put " + t);
            while (lists.size() == MAX) {
                producer.await();
            }
            lists.add(t);
            ++count;
            consumer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        T t = null;

        try {
            lock.lock();

            while (lists.size() == 0) {
                consumer.await();
            }

            t = lists.removeFirst();
            System.out.println(Thread.currentThread().getName() + " took " + t);
            --count;
            producer.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return t;
    }

    public static void main(String[] args) {
        MyContainer<Integer> container = new MyContainer<>();
        int numOfProducer = 10;
        int numOfConsumer = 5;

        for (int i = 0; i < numOfProducer; i++) {
            new Thread(() -> {
                for (int j = 0; j < 50; j++) {
                    container.put(j);
                }
            }).start();
        }

        for (int i = 0; i < numOfConsumer; i++) {
            new Thread(() -> {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " took " + container.take());
                }
            }).start();
        }
    }
}
