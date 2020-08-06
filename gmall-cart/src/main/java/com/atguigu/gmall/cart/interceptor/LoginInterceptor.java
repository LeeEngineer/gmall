package com.atguigu.gmall.cart.interceptor;

import com.atguigu.gmall.cart.config.JwtProperties;
import com.atguigu.gmall.cart.pojo.UserInfo;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lee_engineer
 * @create 2020-08-05 20:45
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor implements HandlerInterceptor {

    //声明线程的局部变量
    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取登陆头信息
        String userKey = CookieUtils.getCookieValue(request, jwtProperties.getUserKey());
        //如果userKey为空，制作一个userKey放入cookie中
        if (StringUtils.isEmpty(userKey)){
            userKey = UUID.randomUUID().toString();
            CookieUtils.setCookie(request, response, jwtProperties.getUserKey(), userKey, jwtProperties.getExpireTime());
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserKey(userKey);

        // 获取用户的登录信息
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        if (!StringUtils.isEmpty(token)){
            try {
                Map<String, Object> map = JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
                Long userId = Long.valueOf(map.get("userId").toString());
                userInfo.setUserId(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        THREAD_LOCAL.set(userInfo);
        // 这里不做拦截，只为获取用户登录信息，不管有没有登录都要放行
        return true;
    }

    //在视图渲染完成之后执行，经常在完成方法中释放资源
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        THREAD_LOCAL.remove();

    }

    /**
     * 封装了一个获取线程局部变量值的静态方法
     * @return
     */
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }



}
