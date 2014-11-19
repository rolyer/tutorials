package com.tools.monitor.util.thread;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午3:43.
 */
public interface TaskExecutorMBean {
    public int getCorePoolSize();

    public int getMaximumPoolSize();

    public int getPoolSize();

    public int getQueueSize();

    public int getActiveCount();
}
