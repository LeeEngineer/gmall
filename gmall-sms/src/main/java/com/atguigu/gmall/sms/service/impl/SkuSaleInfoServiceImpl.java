package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.mapper.SkuBoundsMapper;
import com.atguigu.gmall.sms.mapper.SkuFullReductionMapper;
import com.atguigu.gmall.sms.mapper.SkuLadderMapper;
import com.atguigu.gmall.sms.service.SkuSaleInfoService;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:31
 */
@Service
public class SkuSaleInfoServiceImpl implements SkuSaleInfoService {

    @Autowired
    private SkuBoundsMapper skuBoundsMapper;
    @Autowired
    private SkuFullReductionMapper skuFullReductionMapper;
    @Autowired
    private SkuLadderMapper skuLadderMapper;

    @Override
    @Transactional
    public void saveBySku(SkuSaleDto skuSaleDto) {

        //保存积分信息
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        BeanUtils.copyProperties(skuSaleDto,skuBoundsEntity);
        List<Integer> works = skuSaleDto.getWork();
        if (!CollectionUtils.isEmpty(works) && works.size() == 4){
            skuBoundsEntity.setWork(works.get(3) * 8 + works.get(2) * 4 + works.get(1) * 2 + works.get(0));
        }
        skuBoundsMapper.insert(skuBoundsEntity);

        //保存满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuSaleDto,skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuSaleDto.getFullAddOther());
        skuFullReductionMapper.insert(skuFullReductionEntity);

        //保存打折相关信息
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuSaleDto,skuLadderEntity);
        skuLadderEntity.setAddOther(skuSaleDto.getLadderAddOther());
        skuLadderMapper.insert(skuLadderEntity);

    }

    @Override
    public List<ItemSaleVo> getItemSaleInfoBySkuId(Long skuId) {

        List<ItemSaleVo> itemSaleVos = new ArrayList<>();

        //查询积分信息
        SkuBoundsEntity skuBoundsEntity = skuBoundsMapper.selectOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
        ItemSaleVo bounds = new ItemSaleVo();
        bounds.setType("积分");
        bounds.setDesc("赠送" + skuBoundsEntity.getBuyBounds().setScale( 0, BigDecimal.ROUND_UP ).longValue() + "购物积分，赠送" + skuBoundsEntity.getGrowBounds().setScale( 0, BigDecimal.ROUND_UP ).longValue() + "成长积分");
        itemSaleVos.add(bounds);
        //查询满减信息
        SkuFullReductionEntity fullReductionEntity = skuFullReductionMapper.selectOne(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));
        ItemSaleVo fullReduction = new ItemSaleVo();
        fullReduction.setType("满减");
        fullReduction.setDesc("满" + fullReductionEntity.getFullPrice().setScale(2,BigDecimal.ROUND_UP) + "减" + fullReductionEntity.getReducePrice().setScale(2,BigDecimal.ROUND_UP));
        itemSaleVos.add(fullReduction);

        //查询打折信息
        SkuLadderEntity ladderEntity = skuLadderMapper.selectOne(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
        ItemSaleVo ladder = new ItemSaleVo();
        ladder.setType("打折");
        ladder.setDesc("满" + ladderEntity.getFullCount() + "件打" + ladderEntity.getDiscount().divide(new BigDecimal(10)) + "折");
        itemSaleVos.add(ladder);

        return itemSaleVos;

    }

}
