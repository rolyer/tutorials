package com.tools.monitor.util.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午3:38.
 */
public class TaskThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final boolean daemon;
    private final int threadPriority;

    public TaskThreadFactory(String namePrefix, boolean daemon, int priority) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
                .getThreadGroup();
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.threadPriority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        TaskThread t = new TaskThread(group, r, namePrefix
                + threadNumber.getAndIncrement());
        t.setDaemon(daemon);
        t.setPriority(threadPriority);
        return t;
    }
}
