package com.osfocus.www.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

/**
 * Author: Adrian LIU
 * Date: 2020-07-18
 * Desc: Compare the difference of performance between Disruptor, LinkedBlockingQueue and ArrayBlockingQueue
 * implemented Producer and Consumer with single producer and consumer
 * Disruptor took 137ms to handle 10000000 tasks
 * ArrayBlockingQueue took 2113ms to handle 10000000 tasks
 * LinkedBlockingQueue took 1312ms to handle 10000000 tasks
 */
public class DisruptorVSBlockingQueueWithSingleProducerConsumer {
    private static final int NUM_OF_PRODUCERS = 1;
    private static final int NUM_OF_CONSUMERS = 1;
    private static final int NUM_OF_TASKS = 10000000;
    private static final int BUFFER_SIZE = 1024 * 1024;

    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();
        ExecutorService service1 = Executors.newCachedThreadPool();
        SimpleTimeCounter disruptorTimeCount = new SimpleTimeCounter("Disruptor", NUM_OF_PRODUCERS + 1, NUM_OF_TASKS);

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, BUFFER_SIZE, service1, ProducerType.SINGLE, new SleepingWaitStrategy());
        LongEventHandler handler = new LongEventHandler(NUM_OF_TASKS, disruptorTimeCount);

        disruptor.handleEventsWith(handler);

        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventTranslator translator = new LongEventTranslator();

        disruptorTimeCount.start();
        new Thread(() -> {
            for (long i = 0; i < NUM_OF_TASKS; i++) {
                ringBuffer.publishEvent(translator, i);
            }

            disruptorTimeCount.count();
        }).start();

        disruptorTimeCount.waitForTasksCompletion();
        disruptorTimeCount.timeConsumption();
        disruptor.shutdown();
        service1.shutdown();

        SimpleTimeCounter arrayQueueTimeCount = new SimpleTimeCounter("ArrayBlockingQueue", NUM_OF_CONSUMERS, NUM_OF_TASKS);
        BlockingQueue<LongEvent> arrayBlockingQueue = new ArrayBlockingQueue<>(BUFFER_SIZE);

        LongEvent poisonPill = new LongEvent(-1L);
        LongEventProductionStrategy productionStrategy = new LongEventProductionStrategy(0, NUM_OF_TASKS, poisonPill, NUM_OF_CONSUMERS);
        LongEventConsumptionStrategy consumptionStrategy = new LongEventConsumptionStrategy(poisonPill, arrayQueueTimeCount);

        Thread t1 = new Thread(new BlockingQueueProducer<>(arrayBlockingQueue, productionStrategy));
        Thread t2 = new Thread(new BlockingQueueConsumer<>(arrayBlockingQueue, consumptionStrategy));
        t2.start();

        arrayQueueTimeCount.start();
        t1.start();

        arrayQueueTimeCount.waitForTasksCompletion();
        arrayQueueTimeCount.timeConsumption();

        SimpleTimeCounter linkedQueueTimeCount = new SimpleTimeCounter("LinkedBlockingQueue", NUM_OF_CONSUMERS, NUM_OF_TASKS);
        BlockingQueue<LongEvent> linkedBlockingQueue = new LinkedBlockingQueue<>();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consumptionStrategy = new LongEventConsumptionStrategy(poisonPill, linkedQueueTimeCount);
        t1 = new Thread(new BlockingQueueProducer<>(linkedBlockingQueue, productionStrategy));
        t2 = new Thread(new BlockingQueueConsumer<>(linkedBlockingQueue, consumptionStrategy));
        t2.start();

        linkedQueueTimeCount.start();
        t1.start();

        linkedQueueTimeCount.waitForTasksCompletion();
        linkedQueueTimeCount.timeConsumption();
    }
}
