package com.tools.monitor.util.thread;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午3:40.
 */
public class TaskThread  extends Thread {

    private final long creationTime;

    public TaskThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
        this.creationTime = System.currentTimeMillis();
    }

    public TaskThread(ThreadGroup group, Runnable target, String name,
                      long stackSize) {
        super(group, target, name, stackSize);
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * @return the time (in ms) at which this thread was created
     */
    public final long getCreationTime() {
        return creationTime;
    }
}
