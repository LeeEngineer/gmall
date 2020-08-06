package com.atguigu.gmall.ums;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Lee_engineer
 * @create 2020-08-04 13:50
 */
@SpringBootApplication
@EnableFeignClients
public class GmallUmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUmsApplication.class);
    }

}
