package com.atguigu.gmall.cart;

import com.atguigu.gmall.common.bean.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

/**
 * @author Lee_engineer
 * @create 2020-08-10 19:59
 */
public interface GmallCartApi {

    @PostMapping("asyncCart")
    ResponseVo<Object> asyncCart(@RequestBody Set<String> UserIds);

}
