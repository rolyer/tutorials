package com.tools.monitor;

import com.tools.monitor.startup.Server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * An abstract Monitor, covers common attributes and methods of a monitor.
 *
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午2:48.
 */
public abstract class Monitor implements MonitorMBean {
    /**
     * Constructor with the name of monitor
     *
     * @param name
     *            monitor name
     */
//    public Monitor(String name) {
//        this.name = name;
//    }

    /**
     * Constructor with monitor name and file
     *
     * @param name
     *            monitor name
     * @param file
     *            file to be monitored
     */
    public Monitor(String name, File file) {
        this.name = name;
        this.file = file;
    }

    /**
     * Constructor with monitor name and file path
     *
     * @param name
     *            monitor name
     * @param fileName
     *            the path of file to be monitored
     */
    public Monitor(String name, String fileName) {
        this.name = name;
        if (fileName != null) {
            this.file = new File(fileName);
        }
    }

    /**
     * the name of monitor
     */
    protected String name;

    /**
     * the file is been monitored
     */
    protected File file;

    /**
     * all properties monitor wants
     */
    protected Properties properties;

    /**
     * if true, keep running
     */
    protected boolean running = false;

    protected ThreadGroup threadGroup;

    protected long startTime;

    private Map<String, Object> statusMap = new HashMap<String, Object>();

    protected Server server;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * a monitor can be started
     */
    public abstract void start();

    /**
     * a monitor can be stopped
     */
    public abstract void stop();

    protected Executor executor;

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public void setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    public Map<String, Object> getStatus() {
        statusMap.put("Name", getName());
        statusMap.put("Type", getClass().getSimpleName());
        statusMap.put("File", getFile() == null ? null : getFile()
                .getAbsolutePath());
        statusMap.put("StartTime", startTime);
        return statusMap;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String toString() {
        return getStatus().toString();
    }
}
