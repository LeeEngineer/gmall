package com.atguigu.gmall.cart.config;

import com.atguigu.gmall.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author Lee_engineer
 * @create 2020-08-05 21:23
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtProperties {

    private String pubKeyPath;
    private String cookieName;
    private String userKey;

    private Integer expireTime;

    private PublicKey publicKey;

    @PostConstruct
    public void init(){
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("生成公钥和私钥出错");
            e.printStackTrace();
        }
    }

}
