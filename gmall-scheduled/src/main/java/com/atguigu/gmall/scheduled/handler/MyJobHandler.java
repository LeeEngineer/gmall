package com.atguigu.gmall.scheduled.handler;

import com.atguigu.gmall.cart.GmallCartApi;
import com.atguigu.gmall.scheduled.feign.GmallCartClient;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author Lee_engineer
 * @create 2020-08-10 18:24
 */
@Component
public class MyJobHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GmallCartClient cartClient;
    private static final String KEY = "cart:async:defeat";
    private static int flag =  0;

    @XxlJob("test")
    public ReturnT<String> test(String param) throws InterruptedException {

        flag ++;
        System.out.println("任务" + flag + "进入处理器：" + System.currentTimeMillis() + Thread.currentThread().getName());

        Thread.sleep(5000);

        System.out.println("任务" + flag +"执行完毕:" + System.currentTimeMillis());
        return ReturnT.SUCCESS;

    }

    @XxlJob("test2")
    public ReturnT<String> test2(String param){

        System.out.println("测试2开始执行" + Thread.currentThread().getName());
        int i = 1 / 0;
        System.out.println("测试2执行完毕");
        return ReturnT.SUCCESS;
    }

    @XxlJob("asyncCartHandler")
    public ReturnT<String> asyncCartHandler(String param){

        BoundSetOperations<String, String> setOps = redisTemplate.boundSetOps(KEY);
        Set<String> userIds = setOps.members();
        //远程调用cart服务进行mysql同步
        cartClient.asyncCart(userIds);
        //删除原数据
        redisTemplate.delete(KEY);
        System.out.println("任务执行完毕");
        return ReturnT.SUCCESS;
    }
}
