package com.tools.monitor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.log4j.Logger;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午4:28.
 */
public class FolderMonitor extends Monitor implements Runnable,
        MonitorListener {

    private static final Logger LOGGER = Logger
            .getLogger(FolderMonitor.class);
    private static final String THREAD_PREFIX = "FolderMonitor-";

    public static final long DEFAULT_INTERVAL_MILLIS = 1000L;
    public static final long DEFAULT_LOOKBACK_MILLIS = 60000L;

    private long intervalMillis = DEFAULT_INTERVAL_MILLIS;
    private long lookBackMillis = DEFAULT_LOOKBACK_MILLIS;

    /**
     * This list maintains the monitored files and file monitors.
     * <p>
     * key - file name
     * </p>
     * <p>
     * value - started file monitor
     * </p>
     */
    private Map<String, File> fileMonitors = new LinkedHashMap<String, File>();
    /**
     * <p>
     * key - file name pattern
     * </p>
     * <p>
     * value - monitor template
     * </p>
     */
    private Map<String, FileMonitor> fileMonitorTemplates = new LinkedHashMap<String, FileMonitor>();
    private Thread thread;

    public FolderMonitor(String name, File dir) {
        super(name, dir);
    }

    private void monitor(File... files) {
        if (files.length > 0) {

            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }

                String fileName = file.getName();
                if (fileMonitors.containsKey(fileName)) { // already
                    // monitored
                    continue;
                }

                FileMonitor template = fileNameFilter(fileName); // filter by
                // file name
                if (template == null) { // use a template for matched files
                    continue;
                }

				fileMonitors.put(fileName, file);

				LOGGER.info("Create file monitor '" + file.getName()
						+ "' by '" + this.getName() + "'.");

            }
        }
    }

    private FileMonitor fileNameFilter(String fileName) {

        for (Entry<String, FileMonitor> entry : fileMonitorTemplates
                .entrySet()) {
            String namePattern = entry.getKey();
            if (FilenameUtils.wildcardMatch(fileName, namePattern,
                    IOCase.INSENSITIVE)) {
                LOGGER.info("Found monitor template '" + namePattern
                        + "' for log file '" + fileName + "'.");

                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void start() {

        if (thread == null) {

            validateFolder();

            intervalMillis = Constants.getLong(properties,
                    Constants.PROP_INTERVAL, DEFAULT_INTERVAL_MILLIS);
            lookBackMillis = Constants.getLong(properties,
                    Constants.PROP_LOOKBACK, DEFAULT_LOOKBACK_MILLIS);

            try {

                thread = new Thread(threadGroup, this, THREAD_PREFIX
                        + this.getName());
                thread.setDaemon(false);
                thread.start();

                running = true;
                startTime = System.currentTimeMillis();

                properties.put(Constants.PROP_INTERVAL, intervalMillis);
                properties.put(Constants.PROP_LOOKBACK, lookBackMillis);

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            throw new IllegalStateException("Monitor '"+name+"' is running.");
        }
    }

    private void validateFolder() {

        if (file == null) {
            throw new IllegalArgumentException("Empty file");
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("Folder not exist '"
                    + file.getPath() + "'");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Not a valid folder '"
                    + file.getPath() + "'");
        }
    }

    @Override
    public void stop() {
        if (running == false) {
            throw new IllegalStateException("Monitor is not running.");
        }
        running = false;

        if (fileMonitors.size() > 0) {

            // Stop file monitors
           fileMonitors.clear();
        }

        thread = null;
        startTime = -1;

        LOGGER.info(String.format(
                "'%s' has been stopped.",
                this.getName()));
    }

    @Override
    public void run() {

        LOGGER.info("Start monitoring folder '" + file.getAbsolutePath() + "'.");

        while (running) {

            File[] files = file.listFiles();
            
            //TODO: remove file after process.
            
            monitor(files);

            files = null;

            if (!running) {
                break;
            }
            try {
                Thread.sleep(intervalMillis);
            } catch (final InterruptedException ignored) {
            }
        }
    }

    @Override
    public Map<String, Object> getStatus() {
        Map<String, Object> status = super.getStatus();
        status.put("FileMonitors", fileMonitors);
        return status;
    }

    public Map<String, FileMonitor> getFileMonitorTemplates() {
        return fileMonitorTemplates;
    }

    public long getInterval() {
        long interval = Constants.getLong(properties, Constants.PROP_INTERVAL,
                Constants.DEFAULT_INTERVAL_MILLIS);
        return interval;
    }

    public long getLookBack() {
        long interval = Constants.getLong(properties, Constants.PROP_LOOKBACK,
                Constants.DEFAULT_INTERVAL_MILLIS);
        return interval;
    }

    /**
     * On child file monitor stop
     */
    @Override
    public void onStop(Monitor monitor) {
    }

}
