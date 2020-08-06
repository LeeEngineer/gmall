package com.atguigu.gmall.auth.controller;

import com.atguigu.gmall.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lee_engineer
 * @create 2020-08-04 23:39
 */
@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/toLogin")
    public String toLogin(@RequestParam(value = "returnUrl",required = false)String returnUrl, Model model){

        model.addAttribute("returnUrl", returnUrl);
        return "login";

    }

    @PostMapping("/login")
    public String login(@RequestParam("loginName")String loginName,
                        @RequestParam("password")String password,
                        @RequestParam(value = "returnUrl",required = false,defaultValue = "http://gmall.com")String returnUrl,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        authService.login(loginName,password,request,response);

        return "redirect:" + returnUrl;

    }

}
