package com.osfocus.www.singleton;

import java.util.concurrent.TimeUnit;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: In multi-threading environment, as the operation of getInstance() method is not atomic,
 * It will result in unexpected creation of new instances.
 */
public class ProblematicLazySingleton1 {
    private static ProblematicLazySingleton1 INSTANCE;

    public ProblematicLazySingleton1() {}

    public static ProblematicLazySingleton1 getInstance() {
        // operate some business logic here
        if (INSTANCE == null) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            INSTANCE = new ProblematicLazySingleton1();
        }

        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(ProblematicLazySingleton1.getInstance().hashCode());
            }).start();
        }
    }
}
