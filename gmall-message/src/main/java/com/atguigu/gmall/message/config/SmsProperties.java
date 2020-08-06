package com.atguigu.gmall.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lee_engineer
 * @create 2020-08-04 19:29
 */
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
public class SmsProperties {

    private String regionId;
    private String accessKeyId;
    private String secret;
    private String signName;
    private String templateCode;

}
