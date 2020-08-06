package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author Lee_Engineer
 * @email 1191967047@qq.com
 * @date 2020-07-20 18:23:16
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    List<CategoryEntity> getCategoryByPid(Long pid);

    List<CategoryEntity> queryCatogoriesWithSub(Long pid);

    List<CategoryEntity> queryAllCategoriesByCid3(Long cid3);
}

