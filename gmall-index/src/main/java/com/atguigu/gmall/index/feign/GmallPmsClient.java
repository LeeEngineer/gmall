package com.atguigu.gmall.index.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-07-30 20:08
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
