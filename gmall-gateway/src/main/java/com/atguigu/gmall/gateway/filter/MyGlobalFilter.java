package com.atguigu.gmall.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Lee_engineer
 * @create 2020-08-05 18:38
 */
@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("自定义全局过滤器，拦截所有经过网关的请求");
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
