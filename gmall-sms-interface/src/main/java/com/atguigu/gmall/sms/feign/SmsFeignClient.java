package com.atguigu.gmall.sms.feign;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:51
 */
public interface SmsFeignClient {

    @PostMapping("/sms/skuSaleInfo/save")
    ResponseVo<Object> saveBySku(@RequestBody SkuSaleDto skuSaleDto);

    @GetMapping("/sms/skuSaleInfo/sku/{skuId}")
    ResponseVo<List<ItemSaleVo>> getItemSaleInfoBySkuId(@PathVariable("skuId") Long skuId);

}
