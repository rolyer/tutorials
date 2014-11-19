package com.tools.monitor.startup;

import com.tools.monitor.Constants;
import com.tools.monitor.Monitor;
import com.tools.monitor.config.Config;
import com.tools.monitor.util.thread.TaskExecutorMBean;
import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午2:59.
 */
public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class);

    public static final String MBEAN_PREFIX = "Monitor:type=";
    public static final String MONITOR_THREAD_GROUP = "MONITOR_THREAD_GROUP";

    private Config config;
    private MBeanServer beanServer;
    private ThreadGroup threadGroup;

    private Map<String, Monitor> monitors = new LinkedHashMap<String, Monitor>();

    public Server(Config config) {
        this.config = config;
    }

    private void initMBeanServer() {
        beanServer = ManagementFactory.getPlatformMBeanServer();
        if (beanServer == null) {
            if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
                beanServer = (MBeanServer) MBeanServerFactory.findMBeanServer(
                        null).get(0);
            } else {
                beanServer = MBeanServerFactory.createMBeanServer();
            }
        }
        LOGGER.info("MBean server initilized");
    }

    private void initThreadGroup() {
        threadGroup = new ThreadGroup(MONITOR_THREAD_GROUP);
        threadGroup.setDaemon(false);
    }

    public void init() {
        initMBeanServer();
        initThreadGroup();

        // register task executor mbean
        TaskExecutorMBean taskExecutorMBean = (TaskExecutorMBean) config
                .getTaskExecutor();
        registerMBean(taskExecutorMBean, MBEAN_PREFIX
                + "Manage,name=TaskExecutor");
    }

    public void removeMonitor(Monitor monitor) {
        if (monitor != null) {
            monitors.remove(monitor.getName());
        }
    }

    public void registerMonitor(Monitor monitor) {

        if (monitors.containsKey(monitor.getName())
                && monitors.get(monitor.getName()).isRunning()) {
            throw new IllegalStateException("Monitor '" + monitor.getName()
                    + "' is running, can not perform updating.");
        }

        // set server
        monitor.setServer(this);

        // set executor
        if (monitor.getExecutor() == null) {
            monitor.setExecutor(config.getTaskExecutor());
        }

        // set thread group
        if (threadGroup != null) {
            monitor.setThreadGroup(threadGroup);
        }

        monitors.put(monitor.getName(), monitor);
    }

    public void startMonitor(Monitor monitor) {

        if (!monitors.values().contains(monitor)) {
            registerMonitor(monitor);
        }

        LOGGER.info("Start folder monitor " + monitor.getName() + "'.");
        LOGGER.info("File path " + monitor.getFile().getName() + ".");
        monitor.start();
    }

    public void stopMonitor(Monitor monitor) {
        if (monitor.isRunning()) {
            monitor.stop();
        }
    }

    public void start() throws Exception {

        // start monitors
        List<Monitor> monitors = config.getMonitors();
        if (monitors != null && monitors.size() > 0) {
            for (Monitor monitor : monitors) {

                registerMonitor(monitor);

                if ("true".equals(config.getGlobalProperties().getProperty(
                        Constants.AUTO_START_MONITOR))) {
                    startMonitor(monitor);
                } else {
                    ;
                }

            }
        }

        LOGGER.info("Server is running");
    }

    public void destroy() {

        LOGGER.info("Server instance will be stopped.");
        Collection<Monitor> m = monitors.values();
        if (m != null && m.size() > 0) {
            for (Monitor monitor : m) {
                stopMonitor(monitor);
            }
        }

        config.getTaskExecutor().shutdown();

        //TODO: Save Configuration.

        LOGGER.info("Server stopped.");

    }

    public void registerMBean(Object bean, String objectName) {
        try {
            ObjectName name = new ObjectName(objectName);
            if (!beanServer.isRegistered(name)) {
                beanServer.registerMBean(bean, name);
            }
        } catch (Exception e) {
            LOGGER.error("Register mbean '" + objectName + "' failed.");
        }
    }

    public Map<String, Monitor> getMonitors() {
        return monitors;
    }

    public Config getConfig() {
        return config;
    }

}
