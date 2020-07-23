package com.atguigu.gmall.sms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:23
 */
@Data
public class SkuSaleDto {

    private Long skuId;

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
