package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-03 18:04
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
