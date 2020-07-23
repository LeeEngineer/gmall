package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-23 9:43
 */
@Data
public class SkuVo extends SkuEntity {

    private List<SkuAttrValueEntity> saleAttrs;
    private List<String> images;

    //积分相关属性
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;

    //满减相关属性
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

    //打折相关属性
    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;
}
