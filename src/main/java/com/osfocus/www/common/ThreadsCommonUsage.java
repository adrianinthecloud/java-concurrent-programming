package com.osfocus.www.common;

import java.util.Arrays;

public class ThreadsCommonUsage {
    public static void runAndComputeTime(Thread[] ths, String ...args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < ths.length; i++) {
            ths[i].start();
        }

        for (int i = 0; i < ths.length; i++) {
            try {
                ths[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        if (args.length > 0) {
            System.out.println(args[0] + " took " + (end - start) + "ms");
        } else {
            System.out.println("It took " + (end - start) + "ms");
        }
    }


}
