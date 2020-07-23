package com.atguigu.gmall.wms;

import org.mapstruct.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Lee_engineer
 * @create 2020-07-22 13:51
 */
@SpringBootApplication
@MapperScan("com.atguigu.gmall.wms.mapper")
@EnableSwagger2
@EnableFeignClients
@RefreshScope
public class GmallWmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallWmsApplication.class);
    }

}
