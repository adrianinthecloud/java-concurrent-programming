package com.osfocus.www.singleton;

import java.util.concurrent.CountDownLatch;

/**
 * Author: Adrian LIU
 * Date: 2020-06-21
 * Desc: Double-checked Locking Singleton
 * It seems working. However, it has cache coherence.
 * In extremely high concurrent situation, we might encounter cache coherence issue.
 * This is because to optimize the performance, the compiler, on a multiprocessor the processor or the memory
 * might reorder instructions.
 * In fact, Java memory model allows the publication of partially constructed object, that is the assignment of a Singleton object
 * to a variable is performed before the constructor for Singleton is called.
 * Therefore, in the case of first thread entering the critical area and having the instance partially constructed,
 * the subsequent thread checking the INSTANCE == null might have a false result
 * and use the partially initialized INSTANCE directly. This causes subtle bugs.
 * The solution for the problem mentioned above is provided in VolatileDCl class.
 */
public class BrokenDCL {
    private static BrokenDCL INSTANCE;

    private BrokenDCL() {}

    public static BrokenDCL getInstance() {
        // operate some business logic here
        if (INSTANCE == null) {
            synchronized (BrokenDCL.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BrokenDCL();
                }
            }
        }

        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        int numOfThreads = 1000;
        CountDownLatch latch = new CountDownLatch(numOfThreads);

        for (int i = 0; i < numOfThreads; i++) {
            new Thread(() -> {
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(BrokenDCL.getInstance().hashCode());
            }).start();
        }
    }
}
