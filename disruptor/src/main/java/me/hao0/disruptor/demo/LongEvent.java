package me.hao0.disruptor.demo;

/**
 * The demo
 */
public class LongEvent {

    private long value;

    public void set(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongEvent{" +
                "value=" + value +
                '}';
    }
}