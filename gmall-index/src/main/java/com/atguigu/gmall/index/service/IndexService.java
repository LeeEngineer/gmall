package com.atguigu.gmall.index.service;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-30 20:01
 */
public interface IndexService {

    List<CategoryEntity> getLev1Category() throws JsonProcessingException;

    List<CategoryEntity> getSubCategories(Long pid) throws JsonProcessingException;
}
