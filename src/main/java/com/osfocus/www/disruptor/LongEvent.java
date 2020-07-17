package com.osfocus.www.disruptor;

public class LongEvent {
    private long value;

    public LongEvent(long value) {
        this.value = value;
    }

    public LongEvent() {
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
