package com.atguigu.gmall.item.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Lee_engineer
 * @create 2020-08-03 20:28
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(@Value("${threadPool.coreSize}") int coreSize,
                                                 @Value("${threadPool.maxSize}") int maxSize,
                                                 @Value("${threadPool.timeout}")int timeout,
                                                 @Value("${threadPool.blockingSize}") int blockingSize){

        return new ThreadPoolExecutor(coreSize,maxSize,timeout, TimeUnit.SECONDS,new ArrayBlockingQueue<>(blockingSize), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

    }

}
