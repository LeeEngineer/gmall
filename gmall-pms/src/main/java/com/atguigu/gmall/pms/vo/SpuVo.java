package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-22 20:58
 */
@Data
public class SpuVo extends SpuEntity {

    private List<String> spuImages;
    private List<BaseAttrValueVo> baseAttrs;
    private List<SkuVo> skus;

}
