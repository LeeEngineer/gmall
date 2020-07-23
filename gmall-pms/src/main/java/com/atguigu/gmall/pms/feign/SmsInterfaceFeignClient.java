package com.atguigu.gmall.pms.feign;

import com.atguigu.gmall.pms.feign.fallback.SmsInterfaceFeignClientFallback;
import com.atguigu.gmall.sms.feign.SmsFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-07-23 13:55
 */
@FeignClient(value = "sms-service",fallback = SmsInterfaceFeignClientFallback.class)
public interface SmsInterfaceFeignClient extends SmsFeignClient {


}
