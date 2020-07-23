package com.atguigu.gmall.sms.feign;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.dto.SkuSaleDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:51
 */
public interface SmsFeignClient {

    @PostMapping("/sms/skuSaleInfo/save")
    ResponseVo<Object> saveBySku(@RequestBody SkuSaleDto skuSaleDto);

}
