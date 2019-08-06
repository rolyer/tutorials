package com.rolyer.hbase.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @ClassName: HBaseProperties
 * @Package com.rolyer.hbase.starter.config
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/26 16:57
 * @Version V1.0
 */
@ConfigurationProperties("hbase")
public class HBaseProperties {
    private Map<String, String> config;

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
