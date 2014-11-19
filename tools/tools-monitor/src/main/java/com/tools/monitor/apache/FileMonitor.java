package com.tools.monitor.apache;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * File Monitor
 * <p/>
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-17 上午11:19.
 */
public class FileMonitor {

    private static final Logger LOGGER = Logger.getLogger(FileMonitor.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        String rootDir = "/home/rolyer/temp/";
        monitor(rootDir);
    }

    /**
     * start monitor
     *
     * @param rootDir
     */
    public static void monitor(String rootDir) {

        // monitor path
        // String rootDir = "/home/rolyer/temp/";

        // interval time
        long interval = TimeUnit.SECONDS.toMillis(5);

        //
        FileAlterationObserver observer = new FileAlterationObserver(
                rootDir,
                FileFilterUtils.and(
                    FileFilterUtils.fileFileFilter(),
                    FileFilterUtils.suffixFileFilter(".log")),
                null);
        observer.addListener(new FileListener());

        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);

        // start monitor
        try {
            monitor.start();

            LOGGER.info("Start monitor " + rootDir + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
