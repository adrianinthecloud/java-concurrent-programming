package com.osfocus.www.singleton;

import java.util.concurrent.TimeUnit;

/**
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: This implementation of singleton pattern is not really working.
 * This is because multiple threads pass reading INSTANCE is null and competing for the lock, and
 * after the holding thread releasing the lock, they later enter into the creation process.
 */
public class ProblematicLazySingleton2 {
    private static ProblematicLazySingleton2 INSTANCE;

    private ProblematicLazySingleton2() {}

    public static ProblematicLazySingleton2 getInstance() {
        // operate some business logic here
        if (INSTANCE == null) {
            synchronized (ProblematicLazySingleton2.class) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                INSTANCE = new ProblematicLazySingleton2();
            }
        }

        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(ProblematicLazySingleton2.getInstance().hashCode());
            }).start();
        }
    }
}
