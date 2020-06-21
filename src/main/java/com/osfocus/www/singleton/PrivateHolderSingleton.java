package com.osfocus.www.singleton;

import java.util.concurrent.CountDownLatch;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: Singleton Pattern with Inner Class Holder
 * It is one of the perfect versions for Singleton Pattern
 * The static inner class holder won't be load unless it gets called.
 * It is thread-safe as it can only be load once by the JVM.
 */
public class PrivateHolderSingleton {
    private static class InstanceHolder {
        private final static PrivateHolderSingleton INSTANCE = new PrivateHolderSingleton();
    }

    public static PrivateHolderSingleton getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void m() { System.out.println("m"); }

    public static void main(String[] args) {
        int numOfThread = 100;
        CountDownLatch latch = new CountDownLatch(numOfThread);

        for (int i = 0; i < numOfThread; i++) {
            new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(PrivateHolderSingleton.getInstance().hashCode());
            }).start();
        }
    }
}
