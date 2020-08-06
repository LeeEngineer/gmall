package com.atguigu.gmall.sms.controller;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.service.SkuSaleInfoService;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/sku/{skuId}")
    public ResponseVo<List<ItemSaleVo>> getItemSaleInfoBySkuId(@PathVariable("skuId") Long skuId){

        List<ItemSaleVo> itemSaleVos = skuSaleInfoService.getItemSaleInfoBySkuId(skuId);
        return ResponseVo.ok(itemSaleVos);

    }

    @PostMapping("/save")
    @ApiOperation("保存sku营销信息")
    public ResponseVo<Object> saveBySku(@RequestBody SkuSaleDto skuSaleDto){

        skuSaleInfoService.saveBySku(skuSaleDto);
        return ResponseVo.ok();

    }

}
