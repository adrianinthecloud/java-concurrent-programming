package com.osfocus.www.completablefuture;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

// use customized ThreadFactory having customer thread name for better bugs track
public class TravelBookingThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public TravelBookingThreadFactory(String whatFeatureOfGroup) {
        namePrefix = "From TravelBookingThreadFactory's " + whatFeatureOfGroup + "-worker-";
    }

    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        Thread thread = new Thread(null, task, name, 0, false);
        return thread;
    }
}
