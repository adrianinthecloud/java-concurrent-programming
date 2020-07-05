package com.osfocus.www.collections.blockingqueue;

public class PrintEvenNumberStrategy implements IStrategy {
    @Override
    public void output(Object msg) {
        if (msg instanceof Integer) {
            Integer input = (Integer) msg;
            if (input % 2 == 0) System.out.println(Thread.currentThread().getName() + " printed message = " + input);
        }
    }
}
