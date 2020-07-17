package com.osfocus.www.disruptor;

import com.lmax.disruptor.ExceptionHandler;

public class EventExceptionHandler implements ExceptionHandler {

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        System.out.println("handleEventException：" + ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        System.out.println("handleEventException：" + ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        System.out.println("handleOnStartException：" + ex);
    }

}
