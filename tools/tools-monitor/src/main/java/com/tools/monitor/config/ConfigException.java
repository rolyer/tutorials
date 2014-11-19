package com.tools.monitor.config;

/**
 * Created by Rolyer Luo(rolyer.live@gmail.com) on 14-11-14 下午4:02.
 */
public class ConfigException extends Exception {
    private static final long serialVersionUID = -4179404462837164832L;

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
