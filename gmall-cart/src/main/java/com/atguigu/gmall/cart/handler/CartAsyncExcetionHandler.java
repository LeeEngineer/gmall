package com.atguigu.gmall.cart.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Lee_engineer
 * @create 2020-08-07 20:26
 */
@Component
@Slf4j
public class CartAsyncExcetionHandler implements AsyncUncaughtExceptionHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY = "cart:async:defeat";

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {

        log.error("异步调用发生异常，方法：{}，参数：{}。异常信息：{}", method, objects, throwable.getMessage());
        //失败任务存入redis
        String userId = objects[0].toString();
        BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(KEY);
        setOps.add(userId);

    }
}
