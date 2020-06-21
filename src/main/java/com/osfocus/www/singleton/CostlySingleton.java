package com.osfocus.www.singleton;

/**
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: This Singleton Pattern implementation is costly
 * because it synchronizes the entire getInstance method, whose some business logic performance
 * might not be in the critical area. It results in lower performance in certain cases.
 */
public class CostlySingleton {
    private static CostlySingleton INSTANCE;

    private CostlySingleton() {};

    public static synchronized CostlySingleton getInstance() {
        // operate some business logic here
        if (INSTANCE == null) {
            INSTANCE = new CostlySingleton();
        }

        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        CostlySingleton singleton1 = CostlySingleton.getInstance();
        CostlySingleton singleton2 = CostlySingleton.getInstance();

        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2));
    }
}
