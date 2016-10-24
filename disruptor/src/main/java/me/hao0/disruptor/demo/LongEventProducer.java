package me.hao0.disruptor.demo;

import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;

/**
 * The demo producer
 */
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        // Grab the next sequence
        long sequence = ringBuffer.next();
        try {

            // Get the entry in the Disruptor
            LongEvent event = ringBuffer.get(sequence);

            // for the sequence & fill with data
            event.set(bb.getLong(0));
        } finally {

            // publish the demo of the sequence
            ringBuffer.publish(sequence);
        }
    }
}