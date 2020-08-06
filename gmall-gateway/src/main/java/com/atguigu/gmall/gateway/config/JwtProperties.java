package com.atguigu.gmall.gateway.config;

import com.atguigu.gmall.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author Lee_engineer
 * @create 2020-08-05 19:21
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {

    private String pubKeyPath;
    private String cookieName;

    private PublicKey publicKey;

    @PostConstruct
    public void init(){

        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("获取公钥失败",e);
            throw new RuntimeException();
        }


    }

}
