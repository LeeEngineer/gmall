package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.pojo.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Lee_engineer
 * @create 2020-08-05 20:45
 */
@Controller
public class CartController {

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        return userInfo.toString();
    }

}
