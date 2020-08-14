package com.atguigu.gmall.cart.config;

import com.atguigu.gmall.cart.handler.CartAsyncExcetionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;

/**
 * @author Lee_engineer
 * @create 2020-08-07 20:29
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Autowired
    private CartAsyncExcetionHandler cartAsyncExcetionHandler;

    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return cartAsyncExcetionHandler;
    }

}
