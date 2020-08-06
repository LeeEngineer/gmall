package com.atguigu.gmall.auth.service.impl;

import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.feign.GmallUmsClient;
import com.atguigu.gmall.auth.service.AuthService;
import com.atguigu.gmall.common.exception.GmallException;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.IpUtil;
import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.ums.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lee_engineer
 * @create 2020-08-04 23:48
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private GmallUmsClient umsClient;

    @Autowired
    private JwtProperties jwtProperties;


    @Override
    public void login(String loginName, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //验证用户名密码是否正确
        UserEntity user = umsClient.queryUser(loginName, password).getData();
        if (user == null){
            throw new GmallException("用户名或者密码有误");
        }

        //生成token
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("userName", user.getUsername());
        map.put("ip", IpUtil.getIpAddressAtService(request));

        String token = JwtUtils.generateToken(map, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getExpire() * 60);
        CookieUtils.setCookie(request,response,jwtProperties.getUnick(),user.getNickname(),jwtProperties.getExpire() * 60);

    }
}
