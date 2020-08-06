package com.atguigu.gmall.index.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lee_engineer
 * @create 2020-08-02 12:52
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://106.14.208.184:6379");
        config.useSingleServer().setPassword("kflk1994");
//        config.setLockWatchdogTimeout(20);
        return Redisson.create(config);
    }

}
