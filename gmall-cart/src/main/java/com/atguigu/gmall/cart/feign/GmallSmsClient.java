package com.atguigu.gmall.cart.feign;

import com.atguigu.gmall.sms.feign.SmsFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-07 17:58
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends SmsFeignClient {
}
