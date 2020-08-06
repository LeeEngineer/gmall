package com.atguigu.gmall.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.ums.entity.UserEntity;

import java.util.Map;

/**
 * 用户表
 *
 * @author Lee_Engineer
 * @email 1191967047@qq.com
 * @date 2020-08-04 13:46:19
 */
public interface UserService extends IService<UserEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    Boolean checkData(String data, Integer type);

    void sendMsg(String phone);

    void register(UserEntity user, String code);

    UserEntity queryUser(String loginName, String password);
}

