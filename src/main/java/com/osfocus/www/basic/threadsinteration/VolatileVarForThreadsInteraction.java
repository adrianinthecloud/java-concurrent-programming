package com.osfocus.www.basic.threadsinteration;

/**
 * Author: Adrian LIU
 * Date: 2020-06-14
 * Problem statement:
 * using two threads to interactively print out A1B2C3D4E5F6G7
 * implementation: solve the problem using Volatile variable.
 * We don't need lock here, as the variable is visible between threads and
 * we double-check inside the if statement.
 */
public class VolatileVarForThreadsInteraction {
    private volatile static int i = 0;

    public static void main(String args[]) {
        new Thread(() -> {
            Character baseLetter = 'A';
            while (i < 14) {
                if (i % 2 == 0) {
                    if (i < 14) {
                        // double-check to make sure the newest i is < 14
                        System.out.print((char) (baseLetter + i / 2));
                        i++;
                    }
                }
            }
        }).start();

        new Thread(() -> {
            while (i < 14) {
                if (i % 2 == 1) {
                    if (i < 14) {
                        // double-check to make sure the newest i is < 14
                        System.out.print(i / 2 + 1);
                        i++;
                    }
                }
            }
        }).start();
    }
}
