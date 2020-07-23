package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuAttrValueEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-22 21:14
 */
public class BaseAttrValueVo extends SpuAttrValueEntity {

    public void setValueSelected(List<String> attrValues){

        if (!CollectionUtils.isEmpty(attrValues)){
            this.setAttrValue(StringUtils.join(attrValues,","));
        }

    }

}
