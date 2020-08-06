package com.atguigu.gmall.ums.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lee_engineer
 * @create 2020-08-04 22:07
 */
public interface GmallUmsApi {

    @GetMapping("ums/user/query")
    ResponseVo<UserEntity> queryUser(@RequestParam("loginName")String loginName,@RequestParam("password")String password);

}
