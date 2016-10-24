package me.hao0.disruptor.demo;

import com.lmax.disruptor.EventFactory;

/**
 * The demo pre-producer
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    public LongEvent newInstance() {
        return new LongEvent();
    }

}