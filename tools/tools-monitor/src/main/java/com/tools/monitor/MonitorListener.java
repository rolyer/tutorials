package com.tools.monitor;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午4:28.
 */
public interface MonitorListener {
    /**
     * Called when file monitor is stopped.
     */
    public void onStop(Monitor monitor);
}
