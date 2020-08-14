package com.atguigu.gmall.scheduled.feign;

import com.atguigu.gmall.cart.GmallCartApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Lee_engineer
 * @create 2020-08-10 20:02
 */
@FeignClient("cart-service")
public interface GmallCartClient extends GmallCartApi {
}
