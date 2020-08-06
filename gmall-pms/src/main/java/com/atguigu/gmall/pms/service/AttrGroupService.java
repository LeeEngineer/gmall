package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrGroupVo;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author Lee_Engineer
 * @email 1191967047@qq.com
 * @date 2020-07-20 18:23:16
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    List<AttrGroupVo> getAttrGroupWithAttrsByCategoryId(Long categoryId);

    List<ItemGroupVo> queryGroupsBySpuIdAndCid(Long spuId, Long skuId, Long cid);
}

