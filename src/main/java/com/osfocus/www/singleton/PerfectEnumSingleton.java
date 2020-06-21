package com.osfocus.www.singleton;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: This is one of the perfect versions for Singleton Pattern.
 * Credit gives to Joshua Block
 * This implementation is against reflection cracking.
 * This is because JVM handles the creation and invocation of enum constructors internally,
 * which means that it is not possible for Reflection to utilize it.
 * It also remains Singleton after serialized and deserialized.
 */
public enum PerfectEnumSingleton implements Serializable {
    INSTANCE;
    public int i = 10;

    public void m() {}

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
                System.out.println(PerfectEnumSingleton.INSTANCE.hashCode());
            }).start();
        }
    }
}
