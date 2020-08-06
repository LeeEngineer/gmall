package com.atguigu.gmall.ums.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.exception.GmallException;
import com.atguigu.gmall.common.utils.RandomUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.ums.mapper.UserMapper;
import com.atguigu.gmall.ums.entity.UserEntity;
import com.atguigu.gmall.ums.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String PRE_KEY = "ums:register:code:";
    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UserEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UserEntity>()
        );

        return new PageResultVo(page);
    }

    /**
     * 要校验的数据类型：1，用户名；2，手机；3，邮箱
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkData(String data, Integer type) {

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        switch (type){
            case 1: queryWrapper.eq("username",data); break;
            case 2: queryWrapper.eq("phone",data); break;
            case 3: queryWrapper.eq("email",data); break;
            default: return null;
        }
        return baseMapper.selectCount(queryWrapper) == 0;

    }

    @Override
    public void sendMsg(String phone) {

        //校验手机号是否合法
        if (StringUtils.isEmpty(phone) || phone.length() != 11){
            throw new GmallException("手机号不合法");
        }
        //生成验证码
        String code = RandomUtils.getSixBitRandom();
        //验证码放入redis
        redisTemplate.opsForValue().set(PRE_KEY + phone,JSON.toJSONString(code),5, TimeUnit.MINUTES);

        //发送消息给MQ
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);

        rabbitTemplate.convertAndSend("COMMON-MESSAGE-EXCHANGE","message.register",JSON.toJSONString(map));

    }

    @Override
    public void register(UserEntity user, String code) {

        // 1. 校验短信验证码（查询redis中的验证码，和用户输入的code比较）
        String checkCode = JSON.parseObject(redisTemplate.opsForValue().get(PRE_KEY + user.getPhone()),String.class);
        if (!StringUtils.equals(checkCode,code)){
            throw new GmallException("验证码错误");
        }
        //2.校验用户名、邮箱、手机号是否可用
        if (!this.exsitCheck(user)){
            throw new GmallException("用户名、手机号、或邮箱已被注册");
        }
        //3.生成盐
        String salt = UUID.randomUUID().toString().substring(0, 6);
        user.setSalt(salt);
        //4.加盐加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword() + salt));
        //5.用户注册
        user.setLevelId(1l);
        user.setCreateTime(new Date());
        user.setSourceType(1);
        user.setIntegration(1000);
        user.setGrowth(1000);
        user.setStatus(1);
        baseMapper.insert(user);

        //6.删除短信验证码
        redisTemplate.delete(PRE_KEY + user.getPhone());

    }

    @Override
    public UserEntity queryUser(String loginName, String password) {

        // 1.根据登录名查询用户信息（拿到盐）
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginName).or().eq("phone", loginName).or().eq("email", loginName);
        UserEntity user = baseMapper.selectOne(queryWrapper);
        if (user == null){
            return null;
        }
        String salt = user.getSalt();
        if (!StringUtils.equals(DigestUtils.md5Hex(password + salt),user.getPassword())){
            return null;
        }
        return user;

    }

    private Boolean exsitCheck(UserEntity user){

        if (user != null){
            String username = user.getUsername();
            String phone = user.getPhone();
            String email = user.getEmail();
            Boolean b1 = this.checkData(username, 1);
            Boolean b2 = this.checkData(phone, 2);
            Boolean b3 = this.checkData(email, 3);
            if (b1 && b2 && b3){
                return true;
            }
            return false;

        }
        return false;
    }

}