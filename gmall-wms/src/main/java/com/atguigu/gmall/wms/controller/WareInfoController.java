package com.atguigu.gmall.wms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.wms.entity.WareEntity;
import com.atguigu.gmall.wms.service.WareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Lee_engineer
 * @create 2020-07-22 17:59
 */
@RestController
@RequestMapping("/wms/wareinfo")
@Api(tags = "仓库信息")
public class WareInfoController {

    @Autowired
    private WareService wareService;

    @GetMapping("/list")
    public ResponseVo<PageResultVo> listWaresByPage(PageParamVo pageParamVo){

        PageResultVo pageResultVo = wareService.listByPageAndCondition(pageParamVo);
        return ResponseVo.ok(pageResultVo);

    }

}
