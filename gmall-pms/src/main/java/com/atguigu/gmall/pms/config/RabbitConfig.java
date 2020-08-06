package com.atguigu.gmall.pms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Lee_engineer
 * @create 2020-07-30 15:38
 */
@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabiitTemplate(){

        //消息是否到达交换机均执行
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause) -> {
            log.info("(start)生产者消息确认=========================");
            log.info("correlationData:[{}]", correlationData);
            log.info("ack:[{}]", ack);
            log.info("cause:[{}]", cause);
            if (!ack) {
                log.error("消息未到达rabbitmq交换机");
            }
            log.info("(end)生产者消息确认=========================");
        });
        //消息未到达队列时执行
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) -> {
            log.warn("消息没有到达队列，来自于交换机：{}，路由键：{}，消息内容：{}", exchange, routingKey, new String(message.getBody()));
        });

    }

}
