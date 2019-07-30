package com.rolyer.springboothbase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @ClassName: HBaseProperties
 * @Package com.rolyer.springboothbase.config
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/26 16:57
 * @Version V1.0
 */
@Data
@ConfigurationProperties("hbase")
public class HBaseProperties {
    private Map<String, String> config;

}
