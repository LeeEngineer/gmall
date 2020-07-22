package com.atguigu.gmall.pms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lee_engineer
 * @create 2020-07-21 18:53
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssProperties {

    private String accessId;
    private String accessKey;
    private String endpoint;
    private String bucket;

}
