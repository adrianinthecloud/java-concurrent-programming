package com.osfocus.www.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

/***
 * Author: Adrian LIU
 * Date: 2020-07-18
 * Desc: Compare the difference of performance between Disruptor, LinkedBlockingQueue and ArrayBlockingQueue
 * implemented Producer and Consumer with multiple producers and consumers
 * Result:
 * for 1 producer and 1 consumer
 * Disruptor took 137ms to handle 10000000 tasks (refer to DisruptorVSBlockingQueueWithSingleProducerConsumer class)
 * ProducerType.MULTI requires lock during the process of multiple producers producing events
 * while
 * ProducerType.SINGLE does not require lock, which leads to better performance
 *
 * for 2 producers and 2 consumers -->
 * Disruptor took 478ms to handle 10000000 tasks
 * LinkedBlockingQueue took 1920ms to handle 10000000 tasks
 * ArrayBlockingQueue took 1127ms to handle 10000000 tasks
 */
public class DisruptorVSBlockingQueueWithMultiProducerConsumer {
    private static final int NUM_OF_PRODUCERS = 1;
    private static final int NUM_OF_CONSUMERS = 1;
    private static final int NUM_OF_TASKS = 10000000;
    private static final int BUFFER_SIZE = 1024 * 1024;

    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();
        ExecutorService service1 = Executors.newCachedThreadPool();
        SimpleTimeCounter disruptorTimeCount = new SimpleTimeCounter("Disruptor", NUM_OF_PRODUCERS + 1, NUM_OF_TASKS);

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, BUFFER_SIZE, service1, ProducerType.MULTI, new SleepingWaitStrategy());
        Consumer[] consumers = new Consumer[NUM_OF_CONSUMERS];

        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(disruptorTimeCount, NUM_OF_TASKS);
        }

        disruptor.handleEventsWithWorkerPool(consumers);

        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventTranslator translator = new LongEventTranslator();

        int numOfTasksPerProducer = NUM_OF_TASKS / NUM_OF_PRODUCERS;
        int remain = NUM_OF_TASKS % NUM_OF_PRODUCERS;

        CountDownLatch parallelStartLath = new CountDownLatch(NUM_OF_PRODUCERS);

        service1.execute(() -> {
            parallelStartLath.countDown();
            try {
                parallelStartLath.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            disruptorTimeCount.start();;
            for (long i = 0; i < (numOfTasksPerProducer + remain); i++) {
                ringBuffer.publishEvent(translator, i);
            }
            disruptorTimeCount.count();
        });

        for (int i = 1; i < NUM_OF_PRODUCERS; i++) {
            final int k = i;

            service1.execute(() -> {
                parallelStartLath.countDown();
                try {
                    parallelStartLath.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disruptorTimeCount.start();
                int end = (k + 1) * numOfTasksPerProducer;
                for (long start = k * numOfTasksPerProducer; start < end; start++) {
                    ringBuffer.publishEvent(translator, start);
                }
                disruptorTimeCount.count();
            });
        }

        disruptorTimeCount.waitForTasksCompletion();
        disruptorTimeCount.timeConsumption();

        disruptor.shutdown();
        service1.shutdown();

        BlockingQueue<LongEvent> linkedBlockingQueue = new LinkedBlockingQueue<>();
        BlockingQueue<LongEvent> arrayBlockingQueue = new ArrayBlockingQueue<>(BUFFER_SIZE);

        blockingQueueTimeCheck(numOfTasksPerProducer, remain, linkedBlockingQueue, "LinkedBlockingQueue");
        blockingQueueTimeCheck(numOfTasksPerProducer, remain, arrayBlockingQueue, "ArrayBlockingQueue");

        System.out.println("Main End.");
    }

    private static void blockingQueueTimeCheck(int numOfTasksPerProducer, int remain, BlockingQueue<LongEvent> blockingQueue, String nameOfQueue) {
        SimpleTimeCounter queueTimeCount = new SimpleTimeCounter(nameOfQueue, NUM_OF_CONSUMERS, NUM_OF_TASKS);

        LongEvent poison = new LongEvent(-1L);

        int numOfPillEachProducer = (NUM_OF_PRODUCERS < NUM_OF_CONSUMERS) ? (NUM_OF_CONSUMERS / NUM_OF_PRODUCERS) : 1;

        IStrategy<LongEvent> productionStrategy = new LongEventProductionStrategy(0, numOfTasksPerProducer + remain,
                poison, numOfPillEachProducer);

        ExecutorService service2 = Executors.newCachedThreadPool();

        queueTimeCount.start();
        service2.execute(new BlockingQueueProducer<>(blockingQueue, productionStrategy));

        for (int i = 1; i < NUM_OF_PRODUCERS; i++) {
            if (NUM_OF_PRODUCERS > NUM_OF_CONSUMERS) {
                numOfPillEachProducer = (i < NUM_OF_CONSUMERS) ? 1 : 0;
            } else if (i == NUM_OF_PRODUCERS - 1) {
                numOfPillEachProducer = (NUM_OF_CONSUMERS / NUM_OF_PRODUCERS) + (NUM_OF_CONSUMERS % NUM_OF_PRODUCERS);
            }

            productionStrategy = new LongEventProductionStrategy(i * numOfTasksPerProducer, (i + 1) * numOfTasksPerProducer,
                    poison, numOfPillEachProducer);

            service2.submit(new BlockingQueueProducer<>(blockingQueue, productionStrategy));
        }

        IStrategy<LongEvent> consumerStrategy = new LongEventConsumptionStrategy(poison, queueTimeCount);
        for (int i = 0; i < NUM_OF_CONSUMERS; i++) {
            service2.submit(new BlockingQueueConsumer<>(blockingQueue, consumerStrategy));
        }

        queueTimeCount.waitForTasksCompletion();
        queueTimeCount.timeConsumption();
        service2.shutdown();
    }
}
