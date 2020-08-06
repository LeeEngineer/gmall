package com.atguigu.gmall.auth.feign;

import com.atguigu.gmall.ums.api.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-04 23:46
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}
