package com.atguigu.gmall.item.service;

import com.atguigu.gmall.item.vo.ItemVo;

/**
 * @author Lee_engineer
 * @create 2020-08-03 19:56
 */
public interface ItemService {
    ItemVo loadData(Long skuId);
}
