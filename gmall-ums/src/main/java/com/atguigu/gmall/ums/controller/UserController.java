package com.atguigu.gmall.ums.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gmall.ums.entity.UserEntity;
import com.atguigu.gmall.ums.service.UserService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户表
 *
 * @author Lee_Engineer
 * @email 1191967047@qq.com
 * @date 2020-08-04 13:46:19
 */
@Api(tags = "用户表 管理")
@RestController
@RequestMapping("ums/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("query")
    public ResponseVo<UserEntity> queryUser(
            @RequestParam("loginName")String loginName,
            @RequestParam("password")String password){
        UserEntity userEntity = this.userService.queryUser(loginName, password);
        return ResponseVo.ok(userEntity);
    }

    @GetMapping("/toRegister")
    public ModelAndView toRegister(){

        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;

    }

    @PostMapping("/register")
    public ResponseVo<Object> register(UserEntity user,@RequestParam("code")String code){

        userService.register(user,code);
        return ResponseVo.ok();

    }

    @PostMapping("/code")
    public ResponseVo<Object> sendCode(@RequestParam("phone")String phone){

        userService.sendMsg(phone);
        return ResponseVo.ok();

    }


    @GetMapping("/check/{data}/{type}")
    public ResponseVo<Boolean> checkData(@PathVariable("data") String data,@PathVariable("type") Integer type){

        Boolean result = userService.checkData(data,type);
        return ResponseVo.ok(result);

    }


    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUserByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = userService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UserEntity> queryUserById(@PathVariable("id") Long id){
		UserEntity user = userService.getById(id);

        return ResponseVo.ok(user);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UserEntity user){
		userService.save(user);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UserEntity user){
		userService.updateById(user);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		userService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
