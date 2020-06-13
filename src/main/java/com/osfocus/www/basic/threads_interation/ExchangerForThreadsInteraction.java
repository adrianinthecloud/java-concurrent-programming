package com.osfocus.www.basic.threads_interation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using two Exchangers
 */
public class ExchangerForThreadsInteraction {
    private static Thread t1, t2 = null;

    public static void main(String args[]) {
        Exchanger<Integer> intExchanger = new Exchanger<>();
        Exchanger<Character> charExchanger = new Exchanger<>();

        t1 = new Thread(() -> {
            Character baseLetter = 'A';
            for (int i = 0; i < 7; i++) {
                System.out.print((char) (baseLetter+i));
                try {
                    intExchanger.exchange(i);
                    charExchanger.exchange(baseLetter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t2 = new Thread(() -> {
            Character baseLetter = 'A';
            for (int i = 1; i <= 7; i++) {
                System.out.print(i);
                try {
                    intExchanger.exchange(i);
                    charExchanger.exchange(baseLetter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
