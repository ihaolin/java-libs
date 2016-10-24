package me.hao0.disruptor.demo;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;

public class LongEventMain2 {

    public static void main(String[] args) throws Exception {

        // The factory for the event
        LongEventFactory factory = new LongEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = RingBuffer.createMultiProducer(factory,
                bufferSize, new BlockingWaitStrategy());

        BatchEventProcessor<LongEvent> eventProcessor =
                new BatchEventProcessor<>(ringBuffer, ringBuffer.newBarrier(), new LongEventHandler());

        ringBuffer.addGatingSequences(eventProcessor.getSequence());

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l<10000; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
            // Thread.sleep(1000);
        }
        System.err.println("main");
    }
}