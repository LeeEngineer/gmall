package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 * 
 * @author Lee_Engineer
 * @email 1191967047@qq.com
 * @date 2020-07-20 18:23:16
 */
@Mapper
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValueEntity> {

    List<SkuAttrValueEntity> querySearchSkuAttrValueBySkuId(Long skuId);

    List<Map<String, Object>> querySkusJsonBySpuId(Long spuId);
}
