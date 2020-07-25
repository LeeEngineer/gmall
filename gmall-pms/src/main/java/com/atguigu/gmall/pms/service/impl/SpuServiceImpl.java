package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.SmsInterfaceFeignClient;
import com.atguigu.gmall.pms.mapper.SkuMapper;
import com.atguigu.gmall.pms.mapper.SpuDescMapper;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.BaseAttrValueVo;
import com.atguigu.gmall.pms.vo.SkuVo;
import com.atguigu.gmall.pms.vo.SpuVo;
import com.atguigu.gmall.sms.dto.SkuSaleDto;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.SpuMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("spuService")
public class SpuServiceImpl extends ServiceImpl<SpuMapper, SpuEntity> implements SpuService {

    @Autowired
    private SpuAttrValueService spuAttrValueService;
    @Autowired
    private SpuDescService spuDescService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SmsInterfaceFeignClient smsInterfaceFeignClient;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SpuEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<SpuEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public PageResultVo querySpuByCid(Long categoryId, PageParamVo paramVo) {

        //封装查询条件
        QueryWrapper<SpuEntity> queryWrapper = new QueryWrapper<>();
        if (categoryId != 0) {
            //cid不为0时查询指定分类，为0时查询所有分类
            queryWrapper.eq("category_id", categoryId);
        }

        String key = paramVo.getKey();

        // 如果用户输入了检索条件，根据检索条件查
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(t -> t.like("id", key).or().like("name", key));
        }

        IPage<SpuEntity> page = this.page(
                paramVo.getPage(),
                queryWrapper
        );

        return new PageResultVo(page);

    }

    @Override
    @GlobalTransactional
    public void bigSave(SpuVo spuVo) {

        //1 保存spu相关
        //1.1 保存spu表
        Long spuId = this.saveSpu(spuVo);

        //1.2 保存spu_attr_value表
        spuAttrValueService.saveSpuAttrValue(spuVo, spuId);

        //1.3 保存spu_desc表
        spuDescService.saveSpuDesc(spuVo, spuId);

//        int i = 10 /0;

        //2 保存sku相关
        saveSkuInfo(spuVo, spuId);

//        int i = 1 / 0;

    }

    public void saveSkuInfo(SpuVo spuVo, Long spuId) {
        List<SkuVo> skuVos = spuVo.getSkus();
        if (!CollectionUtils.isEmpty(skuVos)) {
            skuVos.forEach(skuVo -> {
                //2.1 保存sku表
                skuVo.setSpuId(spuId);
                skuVo.setBrandId(spuVo.getBrandId());
                skuVo.setCatagoryId(spuVo.getCategoryId());
                List<String> images = skuVo.getImages();
                if (!CollectionUtils.isEmpty(images)) {
                    skuVo.setDefaultImage(StringUtils.isBlank(skuVo.getDefaultImage()) ? images.get(0) : skuVo.getDefaultImage());
                }
                skuMapper.insert(skuVo);
                Long skuVoId = skuVo.getId();

                //2.2 保存sku_attr_value表
                List<SkuAttrValueEntity> saleAttrs = skuVo.getSaleAttrs();
                if (!CollectionUtils.isEmpty(saleAttrs)) {
                    saleAttrs.forEach(saleAttr -> {
                        saleAttr.setSkuId(skuVoId);
                    });
                    skuAttrValueService.saveBatch(saleAttrs);
                }

                //2.3 保存sku_images表
                if (!CollectionUtils.isEmpty(images)) {
                    List<SkuImagesEntity> skuImagesEntities = images.stream().map(image -> {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        skuImagesEntity.setSkuId(skuVoId);
                        skuImagesEntity.setUrl(image);
                        skuImagesEntity.setDefaultStatus(image.equals(skuVo.getDefaultImage()) ? 1 : 0);
                        return skuImagesEntity;
                    }).collect(Collectors.toList());
                    skuImagesService.saveBatch(skuImagesEntities);
                }

                //3 保存折扣相关表
                //远程调用
                SkuSaleDto skuSaleDto = new SkuSaleDto();
                BeanUtils.copyProperties(skuVo, skuSaleDto);
                skuSaleDto.setSkuId(skuVoId);
                smsInterfaceFeignClient.saveBySku(skuSaleDto);

            });
        }
    }


    public Long saveSpu(SpuVo spuVo) {
        spuVo.setCreateTime(new Date());
        spuVo.setUpdateTime(spuVo.getCreateTime());
        this.save(spuVo);
        return spuVo.getId();
    }

}