package com.osfocus.www.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.SimpleTimeZone;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class Main {
    private static final int NUM_OF_PRODUCERS = 10;
    private static final int NUM_OF_CONSUMERS = 5;
    private static final int NUM_OF_TASKS = 100000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, BUFFER_SIZE,
                Executors.defaultThreadFactory(), ProducerType.MULTI, new BlockingWaitStrategy());

        Consumer[] consumers = new Consumer[NUM_OF_CONSUMERS];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer();
        }

        disruptor.handleEventsWithWorkerPool(consumers);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        disruptor.start();

        ByteBuffer bb = ByteBuffer.allocate(8);

        ExecutorService service = Executors.newCachedThreadPool();

        EventTranslatorOneArg<LongEvent, Long> translator = new EventTranslatorOneArg<>() {
            @Override
            public void translateTo(LongEvent longEvent, long l, Long aLong) {
                longEvent.setValue(aLong);
            }
        };

        SimpleTimeCounter disruptorTimeCount = new SimpleTimeCounter("Disruptor", NUM_OF_PRODUCERS, NUM_OF_TASKS);
        LongAdder longAdder = new LongAdder();
        disruptorTimeCount.start();
        for (int i = 0; i < NUM_OF_PRODUCERS; i++) {
            service.submit(() -> {
                while (longAdder.longValue() < NUM_OF_TASKS) {
                    ringBuffer.publishEvent(translator, longAdder.longValue());
                    longAdder.increment();
                }
                disruptorTimeCount.count();
            });
        }


        disruptorTimeCount.waitForTasksCompletion();

        service.shutdown();
        disruptor.shutdown();

//        LinkedBlockingQueue<Long> arrayBlockingQueue = new LinkedBlockingQueue<>();


        System.out.println("Main End.");
    }
}
