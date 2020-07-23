package com.atguigu.gmall.sms.controller;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.service.SkuSaleInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:25
 */
@Api(tags = "sku营销信息")
@RestController
@RequestMapping("/sms/skuSaleInfo")
public class SkuSaleInfoController {

    @Autowired
    private SkuSaleInfoService skuSaleInfoService;

    @PostMapping("/save")
    @ApiOperation("保存sku营销信息")
    public ResponseVo<Object> saveBySku(@RequestBody SkuSaleDto skuSaleDto){

        skuSaleInfoService.saveBySku(skuSaleDto);
        return ResponseVo.ok();

    }

}
