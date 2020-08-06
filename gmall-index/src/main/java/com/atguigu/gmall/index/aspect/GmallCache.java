package com.atguigu.gmall.index.aspect;

import java.lang.annotation.*;

/**
 * @author Lee_engineer
 * @create 2020-08-02 13:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GmallCache {

    //缓存前缀
    String prefix() default "";

    //缓存过期时间,单位 分钟
    int timeout() default 5;

    //防止缓存雪崩，指定过期时间随机值的范围
    int random() default 5;

    //防止缓存击穿，添加分布式锁，指定锁的名称
    String lock() default "lock";

}
