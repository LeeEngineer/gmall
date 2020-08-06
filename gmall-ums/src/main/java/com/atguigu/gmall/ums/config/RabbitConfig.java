package com.atguigu.gmall.ums.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Lee_engineer
 * @create 2020-08-04 19:45
 */
@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        // 确认消息是否到达交换机
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack){
                log.warn("消息没有到达交换机：" + cause);
            }
        });

        // 确认消息是否到达队列，到达队列该方法不执行
        this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.warn("消息没有到达队列，来自于交换机：{}，路由键：{}，消息内容：{}", exchange, routingKey, new String(message.getBody()));
        });
    }

}
