package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SpuAttrValueEntity;
import com.atguigu.gmall.pms.mapper.AttrMapper;
import com.atguigu.gmall.pms.mapper.SkuAttrValueMapper;
import com.atguigu.gmall.pms.mapper.SpuAttrValueMapper;
import com.atguigu.gmall.pms.vo.AttrGroupVo;
import com.atguigu.gmall.pms.vo.AttrValueVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.pms.mapper.AttrGroupMapper;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrMapper attrMapper;

    @Autowired
    private SpuAttrValueMapper spuAttrValueMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<AttrGroupEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public List<AttrGroupVo> getAttrGroupWithAttrsByCategoryId(Long categoryId) {

        QueryWrapper<AttrGroupEntity> attrGroupEntityQueryWrapper = new QueryWrapper<>();
        attrGroupEntityQueryWrapper.eq("category_id",categoryId);
        //根据分类id查询属性组
        List<AttrGroupEntity> attrGroupEntities = baseMapper.selectList(attrGroupEntityQueryWrapper);

        if (CollectionUtils.isEmpty(attrGroupEntities)){
            return null;
        }

        //根据属性组查询spu属性
        List<AttrGroupVo> attrGroupVos = attrGroupEntities.stream().map(attrGroupEntity -> {
            AttrGroupVo attrGroupVo = new AttrGroupVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupVo);
            attrGroupVo.setAttrEntities(attrMapper.selectList(new QueryWrapper<AttrEntity>()
                    .eq("group_id", attrGroupEntity.getId()).eq("type", 1)));
            return attrGroupVo;
        }).collect(Collectors.toList());
        return attrGroupVos;

    }

    @Override
    public List<ItemGroupVo> queryGroupsBySpuIdAndCid(Long spuId, Long skuId, Long cid) {

        //根据cid查询分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("category_id", cid));
        if (CollectionUtils.isEmpty(attrGroupEntities)){
            return null;
        }

        //遍历各个分组下的属性
        return attrGroupEntities.stream().map(group -> {
            ItemGroupVo itemGroupVo = new ItemGroupVo();
            itemGroupVo.setGroupId(group.getId());
            itemGroupVo.setGroupName(group.getName());

            List<AttrEntity> attrEntities = this.attrMapper.selectList(new QueryWrapper<AttrEntity>().eq("group_id", group.getId()));
            if (!CollectionUtils.isEmpty(attrEntities)){
                List<Long> attrIds = attrEntities.stream().map(AttrEntity::getId).collect(Collectors.toList());
                //根据分组及spuId查询spu属性
                List<SpuAttrValueEntity> spuAttrValueEntities = spuAttrValueMapper.selectList(new QueryWrapper<SpuAttrValueEntity>().eq("spu_id", spuId).in("attr_id", attrIds));
                //根据分组及skuId查询sku属性
                List<SkuAttrValueEntity> skuAttrValueEntities = skuAttrValueMapper.selectList(new QueryWrapper<SkuAttrValueEntity>().eq("sku_id", skuId).in("attr_id", attrIds));

                List<AttrValueVo> attrValueVos = new ArrayList<>();

                if (!CollectionUtils.isEmpty(spuAttrValueEntities)){
                    List<AttrValueVo> spuValueVos = spuAttrValueEntities.stream().map(spuAttrValueEntity -> {
                        AttrValueVo attrValueVo = new AttrValueVo();
                        attrValueVo.setAttrId(spuAttrValueEntity.getAttrId());
                        attrValueVo.setAttrName(spuAttrValueEntity.getAttrName());
                        attrValueVo.setAttrValue(spuAttrValueEntity.getAttrValue());
                        return attrValueVo;
                    }).collect(Collectors.toList());
                    attrValueVos.addAll(spuValueVos);
                }
                if (!CollectionUtils.isEmpty(skuAttrValueEntities)){
                    List<AttrValueVo> skuValueVos = skuAttrValueEntities.stream().map(skuAttrValueEntity -> {
                        AttrValueVo attrValueVo = new AttrValueVo();
                        attrValueVo.setAttrId(skuAttrValueEntity.getAttrId());
                        attrValueVo.setAttrName(skuAttrValueEntity.getAttrName());
                        attrValueVo.setAttrValue(skuAttrValueEntity.getAttrValue());
                        return attrValueVo;
                    }).collect(Collectors.toList());
                    attrValueVos.addAll(skuValueVos);
                }
                itemGroupVo.setAttrs(attrValueVos);
            }
            System.out.println(itemGroupVo);
            return itemGroupVo;
        }).collect(Collectors.toList());

    }

}