package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.sms.feign.SmsFeignClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-03 18:04
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends SmsFeignClient {
}
