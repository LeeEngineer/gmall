package com.atguigu.gmall.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.wms.entity.WareEntity;

import java.util.List;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author Lee_Engineer
 * @email 1191967047@qq.com
 * @date 2020-07-22 16:46:00
 */
public interface WareService extends IService<WareEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    PageResultVo listByPageAndCondition(PageParamVo paramVo);

}

