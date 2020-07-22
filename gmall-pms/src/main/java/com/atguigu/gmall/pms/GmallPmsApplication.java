package com.atguigu.gmall.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Lee_engineer
 * @create 2020-07-20 18:25
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients
@MapperScan("com.atguigu.gmall.pms.mapper")
@RefreshScope
public class GmallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPmsApplication.class);
    }
}
