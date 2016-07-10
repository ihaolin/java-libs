package org.apache.zookeeper.book.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CountDownLatch;

public class TaskObject {

    private static final Logger LOG = LoggerFactory.getLogger(TaskObject.class);
    private String task;
    private String taskName;
    private boolean done = false;
    private boolean successful = false;
    private CountDownLatch latch = new CountDownLatch(1);

    String getTask() {
        return task;
    }

    void setTask(String task) {
        this.task = task;
    }

    void setTaskName(String name) {
        this.taskName = name;
    }

    String getTaskName() {
        return taskName;
    }

    public void setStatus(boolean status) {
        successful = status;
        done = true;
        latch.countDown();
    }

    public void waitUntilDone() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            LOG.warn("InterruptedException while waiting for task to get done");
        }
    }

    public synchronized boolean isDone() {
        return done;
    }

    public synchronized boolean isSuccessful() {
        return successful;
    }

}