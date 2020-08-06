package com.atguigu.gmall.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lee_engineer
 * @create 2020-08-04 23:48
 */
public interface AuthService {
    void login(String loginName, String password, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
