package com.tools.monitor;

import java.util.Properties;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午3:49.
 */
public class Constants {

    private Constants() {
    }

    public static final int DEFAULT_INTERVAL_MILLIS = 250;
    public static final String PROP_INTERVAL = "PROP_INTERVAL";
    public static final String PROP_LOOKBACK = "PROP_LOOKBACK";
    public static final String AUTO_START_MONITOR = "AutoStartMonitor";

    public static long getLong(Properties prop, String name, long defVal) {
        long val = defVal;
        if (prop != null) {
            String valStr = prop.getProperty(name);
            try {
                if (valStr == null) {
                    val = (Long) prop.get(name);
                } else {
                    val = Long.parseLong(valStr);
                }
            } catch (Exception e) {
            }
        }
        return val;
    }
}
