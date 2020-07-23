package com.atguigu.gmall.wms.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;

import com.atguigu.gmall.wms.mapper.WareMapper;
import com.atguigu.gmall.wms.entity.WareEntity;
import com.atguigu.gmall.wms.service.WareService;
import org.springframework.util.StringUtils;


@Service("wareService")
public class WareServiceImpl extends ServiceImpl<WareMapper, WareEntity> implements WareService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<WareEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<WareEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public PageResultVo listByPageAndCondition(PageParamVo paramVo) {

        QueryWrapper<WareEntity> queryWrapper = new QueryWrapper<>();

        String key = paramVo.getKey();
        if (!StringUtils.isEmpty(key)){
            queryWrapper.like("name",key).or().like("address",key);
        }

        IPage<WareEntity> page = this.page(paramVo.getPage(),queryWrapper);

        return new PageResultVo(page);

    }

}