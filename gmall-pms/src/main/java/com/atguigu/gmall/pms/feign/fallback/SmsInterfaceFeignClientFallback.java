package com.atguigu.gmall.pms.feign.fallback;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.feign.SmsInterfaceFeignClient;
import com.atguigu.gmall.sms.dto.SkuSaleDto;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:59
 */
@Component
public class SmsInterfaceFeignClientFallback implements SmsInterfaceFeignClient {
    @Override
    public ResponseVo<Object> saveBySku(SkuSaleDto skuSaleDto) {
        return ResponseVo.fail("营销信息保存失败");
    }

    @Override
    public ResponseVo<List<ItemSaleVo>> getItemSaleInfoBySkuId(Long skuId) {
        return null;
    }
}
