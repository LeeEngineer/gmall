package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-22 19:16
 */
@Data
public class AttrGroupVo extends AttrGroupEntity {

    private List<AttrEntity> attrEntities;

}
