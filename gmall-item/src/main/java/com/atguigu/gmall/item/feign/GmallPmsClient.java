package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-03 18:03
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
