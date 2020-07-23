package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.mapper.SkuBoundsMapper;
import com.atguigu.gmall.sms.mapper.SkuFullReductionMapper;
import com.atguigu.gmall.sms.mapper.SkuLadderMapper;
import com.atguigu.gmall.sms.service.SkuSaleInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

}
