package com.rolyer.springboothbase.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: HbaseConfig
 * @Package com.rolyer.springboothbase.config
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/26 16:36
 * @Version V1.0
 */
@Configuration
@EnableConfigurationProperties(HBaseProperties.class)
public class HbaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(HbaseConfig.class);

    private final HBaseProperties properties;

    private static Connection connection = null;
    private static ExecutorService pool = Executors.newScheduledThreadPool(20);

    public HbaseConfig(HBaseProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HbaseTemplate hbaseTemplate(){
        HbaseTemplate hbaseTemplate = new HbaseTemplate(configuration());
        hbaseTemplate.setAutoFlush(true);

        return hbaseTemplate;
    }

    @Bean
    public Admin admin(){
        if(connection == null){
            try {
                connection = ConnectionFactory.createConnection(configuration(), pool);
                 return connection.getAdmin();
            } catch (IOException e) {
                logger.error("HbaseUtils实例初始化失败！错误信息为：" + e.getMessage(), e);
            }
        }

        return null;
    }


    public org.apache.hadoop.conf.Configuration configuration() {

        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();

        Map<String, String> config = properties.getConfig();
        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }

        return configuration;
    }
}
