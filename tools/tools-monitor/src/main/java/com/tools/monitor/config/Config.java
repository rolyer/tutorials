package com.tools.monitor.config;

import com.tools.monitor.Monitor;
import com.tools.monitor.util.thread.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14.
 */
public class Config {

    private List<Monitor> monitors = new LinkedList<Monitor>();
    private Properties globalProperties = new Properties();

    private TaskExecutor taskExecutor;

    public Config() {
        buildDefaultTaskExecutor();
    }

    private void buildDefaultTaskExecutor() {
        TaskQueue taskQueue = new TaskQueue();
        TaskThreadFactory taskThreadFactory = new TaskThreadFactory(
                "Processor-", true, Thread.NORM_PRIORITY);
        taskExecutor = new TaskExecutor(3, 3, 60, TimeUnit.SECONDS, taskQueue,
                taskThreadFactory);
    }

    public List<Monitor> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<Monitor> monitors) {
        this.monitors = monitors;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public Properties getGlobalProperties() {
        return globalProperties;
    }

    @Override
    public String toString() {
        return monitors.toString();
    }
}
