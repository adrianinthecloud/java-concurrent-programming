package com.osfocus.www.basic.containers;

import com.osfocus.www.common.ThreadsCommonUsage;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

// Synchronized -> Lock escalation
// LongAdder -> Idea of the CAS segment lock
// AtomicLong -> CAS
// when the number of threads is high, e.g. 10000
//      Performance: LongAdder(2125ms) > synchronized(8166ms) > AtomicLong(20328ms)
// when the number of threads is moderate, e.g. 1000
//      Performance: LongAdder(350ms) > AtomicLong(2049ms) > synchronized(2542ms)
// when the number of threads is small, e.g. 100
//      Performance: LongAdder(100ms) > AtomicLong(216ms) > Synchronized(494ms)
public class C06_AtomicVsSyncVsLongAdder {
    private static final int NUM_OF_THREADS = 10000;
    private static final int INC_EACH_THREAD = 100000;

    private static long count = 0L;
    private static AtomicLong atomicLong = new AtomicLong();
    private static LongAdder longAdder = new LongAdder();

    public static void main(String[] args) {
        Thread[] threads = new Thread[NUM_OF_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INC_EACH_THREAD; j++) {
                    synchronized (C06_AtomicVsSyncVsLongAdder.class) {
                        count++;
                    }
                }
            });
        }

        ThreadsCommonUsage.runAndComputeTime(threads, "Synchronized Count");

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INC_EACH_THREAD; j ++) {
                    atomicLong.incrementAndGet();
                }
            });
        }

        ThreadsCommonUsage.runAndComputeTime(threads, "AtomicLong Count");

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INC_EACH_THREAD; j ++) {
                    longAdder.increment();
                }
            });
        }

        ThreadsCommonUsage.runAndComputeTime(threads, "LongAdder Count");
    }
}
