package com.rolyer.springboothbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @ClassName: SpringbootHbaseApp
 * @Package com.rolyer.springboothbase
 * @Description: ${TOTO} (用一句话描述该文件做什么)
 * @Author rolyer
 * @Date 2019/7/26 16:31
 * @Version V1.0
 */
@SpringBootApplication
@EnableWebMvc
public class SpringbootHbaseApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootHbaseApp.class);
    }

}
