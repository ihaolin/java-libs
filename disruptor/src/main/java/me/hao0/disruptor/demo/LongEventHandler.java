package me.hao0.disruptor.demo;

import com.lmax.disruptor.EventHandler;

/**
 * The demo consumer
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event: " + event);
    }

}