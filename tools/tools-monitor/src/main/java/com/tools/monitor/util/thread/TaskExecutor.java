package com.tools.monitor.util.thread;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午3:42.
 */
public class TaskExecutor extends ThreadPoolExecutor implements
        TaskExecutorMBean {

    private static final Logger LOGGER = Logger.getLogger(TaskExecutor.class);

    public TaskExecutor(int corePoolSize, int maximumPoolSize,
                        long keepAliveTime, TimeUnit unit,
                        BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                handler);
    }

    public TaskExecutor(int corePoolSize, int maximumPoolSize,
                        long keepAliveTime, TimeUnit unit,
                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                        RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, handler);
    }

    public TaskExecutor(int corePoolSize, int maximumPoolSize,
                        long keepAliveTime, TimeUnit unit,
                        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, new RejectHandler());
    }

    public TaskExecutor(int corePoolSize, int maximumPoolSize,
                        long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new RejectHandler());
    }

    private static class RejectHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r,
                                      ThreadPoolExecutor executor) {
            throw new RejectedExecutionException();
        }

    }

    private Map<Runnable, Thread> activeTasks = new HashMap<Runnable, Thread>();
    private Map<String, Runnable> recentlyRunnable = new HashMap<String, Runnable>();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        activeTasks.put(r, t);
        recentlyRunnable.put(t.getName(), r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        activeTasks.remove(r);
    }

    @Override
    public void execute(Runnable command) {
        LOGGER.trace("New task executing, " + command);
        super.execute(command);
    }

    public int getQueueSize() {
        return getQueue().size();
    }

    public final Map<Runnable, Thread> getActiveTasks() {
        return activeTasks;
    }

    public final Map<String, Runnable> getRecentlyRunnable() {
        return recentlyRunnable;
    }
}
