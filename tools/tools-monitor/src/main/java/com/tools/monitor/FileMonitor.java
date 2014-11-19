package com.tools.monitor;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午4:33.
 */
public class FileMonitor extends Monitor {

    /**
     * Constructor with the name of monitor
     *
     * @param name
     *            monitor name
     */
    public FileMonitor(String name) {
        this(name, null);
    }

    /**
     * Constructor with monitor name and file
     *
     * @param name
     *            monitor name
     * @param file
     *            the path of file to be monitored
     */
    public FileMonitor(String name, String file) {
        super(name, file);
    }

    /**
     * start or re-start monitoring
     */
    @Override
    public void start() {
    }

    /**
     * stop monitoring, the tailer of this monitor will be stopped also
     */
    @Override
    public void stop() {
    }
}
