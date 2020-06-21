package com.osfocus.www.singleton;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: This class demonstrates how to prevent anyone breaking the Singleton Pattern
 * by cloning a new instance.
 */
class SuperClass implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

public class NotCloneableSingleton extends SuperClass {
    private static NotCloneableSingleton INSTANCE = new NotCloneableSingleton();

    private NotCloneableSingleton() {};

    public static NotCloneableSingleton getInstance() { return INSTANCE; };

    @Override
    public NotCloneableSingleton clone() {
        return INSTANCE;
    }

    public static void main(String args[]) {
        NotCloneableSingleton instance1 = NotCloneableSingleton.getInstance();
        NotCloneableSingleton instance2 = null;
        instance2 = instance1.clone();

        System.out.println("equals? " + (instance1 == instance2));
        System.out.println("instance1 hashcode = " + instance1.hashCode());
        System.out.println("instance2 hashcode = " + instance2.hashCode());
    }
}
