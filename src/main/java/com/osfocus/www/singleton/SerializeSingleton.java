package com.osfocus.www.singleton;

import java.io.*;

/***
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: This class demonstrate how to implement Singleton Pattern with serialization consistency.
 */
public class SerializeSingleton implements Serializable {

    private static final long serialVersionUID = 7475409860388122562L;

    private static SerializeSingleton INSTANCE = new SerializeSingleton();

    private String name;
    private int count;

    private SerializeSingleton() {};

    public static SerializeSingleton getInstance() { return INSTANCE; };

    public void m() {
        System.out.println("Do something in m().");
    }

    // readResolve is used for replacing the object read from the stream.
    // This prevents anybody creating new instance by serializing and deserializing the singleton.
    protected Object readResolve() {
        return INSTANCE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static void main(String args[]) {
        SerializeSingleton instance1 = SerializeSingleton.getInstance();
        instance1.setCount(999);
        instance1.setName("Adrian OK");
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("file.txt"));) {
            out.writeObject(instance1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SerializeSingleton instance2 = null;
        try (ObjectInput in = new ObjectInputStream(new FileInputStream("file.txt"));) {
            instance2 = (SerializeSingleton) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Equals? " + (instance1 == instance2));
        System.out.println("instance1 hashcode = " + instance1.hashCode());
        System.out.println("instance2 hashcode = " + instance2.hashCode());
        System.out.println("instance1 = " + instance1.getCount() + " " + instance1.getName());
        System.out.println("instance2 = " + instance2.getCount() + " " + instance2.getName());
    }
}
