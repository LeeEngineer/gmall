package com.atguigu.gmall.index.aspect;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Lee_engineer
 * @create 2020-08-02 13:27
 */
@Component
@Aspect
public class GmallCacheAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(com.atguigu.gmall.index.aspect.GmallCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        // 获取方法签名
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        //获取目标方法
        Method method = signature.getMethod();
        //获取目标方法上的注解
        GmallCache gmallCache = method.getAnnotation(GmallCache.class);
        //获取缓存前缀
        String prefix = gmallCache.prefix();
        //获取目标方法形参
        Object[] args = joinPoint.getArgs();
        //拼装分布式锁key
        String lockName = gmallCache.lock() + (ArrayUtils.isEmpty(args) ? "" : Arrays.asList(args));
        //拼装缓存key
        String key = prefix + (ArrayUtils.isEmpty(args)? "level1" :"level2:" + Arrays.asList(args));
        // 获取方法的返回值类型
        Class returnType = signature.getReturnType();

        //先查询缓存
        String cache = redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(cache)){
            return JSON.parseObject(cache,returnType);
        }

        //加分布式锁
        RLock rLock = redissonClient.getLock(lockName);
        rLock.lock(40,TimeUnit.SECONDS);

        //再次判断缓存中是否已有数据
        try {
            cache = redisTemplate.opsForValue().get(key);

            if (!StringUtils.isEmpty(cache)){
                return JSON.parseObject(cache,returnType);
            }

            //执行目标方法
            Object result = joinPoint.proceed(args);

            Thread.sleep(35000);

            //把目标方法放入缓存中
            //获取缓存过期时间
            int timeout = gmallCache.timeout();
            int random = gmallCache.random();
            this.redisTemplate.opsForValue().set(key,JSON.toJSONString(result),timeout + new Random().nextInt(random), TimeUnit.MINUTES);

            return result;
        } finally {
            rLock.unlock();
        }

    }

}
