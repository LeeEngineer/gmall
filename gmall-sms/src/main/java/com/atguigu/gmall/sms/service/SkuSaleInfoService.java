package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.vo.ItemSaleVo;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:30
 */
public interface SkuSaleInfoService {

    void saveBySku(SkuSaleDto skuSaleDto);


    List<ItemSaleVo> getItemSaleInfoBySkuId(Long skuId);
}
