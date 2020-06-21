package com.osfocus.www.singleton;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: This file demonstrate how I use Reflection to break some Singleton implementation
 * Solution: Use enum to implement the pattern. Please refer to PerfectEnumSingleton class
 */
public class ReflectionCracker {
    public static void main(String[] args) {
        GreedySingleton instance1 = GreedySingleton.getInstance();
        GreedySingleton.class.getDeclaredConstructors()[0].setAccessible(true);
        GreedySingleton instance2 = new GreedySingleton();

        System.out.println("Equals? " + (instance1 == instance2));

        System.out.println("instance1 hashcode = " + instance1.hashCode());
        System.out.println("instance2 hashcode = " + instance2.hashCode());
    }
}
