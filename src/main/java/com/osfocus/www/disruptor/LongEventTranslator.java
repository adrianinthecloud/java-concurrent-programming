package com.osfocus.www.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;

public class LongEventTranslator implements EventTranslatorOneArg<LongEvent, Long>  {
    @Override
    public void translateTo(LongEvent evt, long l, Long value) {
        evt.setValue(value);
    }
}
