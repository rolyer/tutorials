package com.rolyer.hbase.starter.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
 * @ClassName: HbaseAutoConfiguration
 * @Package com.rolyer.hbase.starter.config
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/8/5 15:10
 * @Version V1.0
 */
@Configuration
@EnableConfigurationProperties(HBaseProperties.class)
@ConditionalOnClass({HbaseTemplate.class, Admin.class})
@ConditionalOnProperty(prefix = "hbase", value = "enabled", matchIfMissing = true)
public class HbaseAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HbaseAutoConfiguration.class);

    @Autowired
    private HBaseProperties hbaseProperties;

    private static Connection connection = null;
    private static ExecutorService pool = Executors.newScheduledThreadPool(20);


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

        Map<String, String> config = hbaseProperties.getConfig();
        Set<String> keySet = config.keySet();
        for (String key : keySet) {
            configuration.set(key, config.get(key));
        }

        return configuration;
    }
}
