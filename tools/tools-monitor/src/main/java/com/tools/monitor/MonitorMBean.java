package com.tools.monitor;

import java.util.Map;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午2:52.
 */
public interface MonitorMBean {
    public Map<String, Object> getStatus();

    public boolean isRunning();

    public void start();

    public void stop();
}
