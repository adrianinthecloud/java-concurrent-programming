package com.osfocus.www.singleton;

import java.io.Serializable;

/**
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: singleton pattern uses Greedy initialization.
 */
public class GreedySingleton implements Serializable {
    private static GreedySingleton INSTANCE = new GreedySingleton();

    public static GreedySingleton getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        GreedySingleton singleton1 = GreedySingleton.getInstance();
        GreedySingleton singleton2 = GreedySingleton.getInstance();

        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2));
    }
}
