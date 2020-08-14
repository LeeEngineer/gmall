package com.atguigu.gmall.cart.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-07 17:57
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
