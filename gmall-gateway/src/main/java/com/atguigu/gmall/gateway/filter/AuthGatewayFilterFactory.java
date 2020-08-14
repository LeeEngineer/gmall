package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.utils.IpUtil;
import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.gateway.config.JwtProperties;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Lee_engineer
 * @create 2020-08-05 18:43
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.KeyValueConfig > {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 一定要重写构造方法
     * 告诉父类，这里使用PathConfig对象接收配置内容
     */
    public AuthGatewayFilterFactory() {
        super(KeyValueConfig.class);
    }

    @Override
    public GatewayFilter apply(KeyValueConfig config) {

        return new GatewayFilter() {

            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                // 1.判断当前请求路径在不在名单中，不在直接放行
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();
                String curPath = request.getURI().getPath();

                List<String> pathes = config.getPathes();
                if (pathes.stream().allMatch(path -> curPath.indexOf(path) == -1)){
                    //所有路径都不匹配
                    return chain.filter(exchange);
                }

                //2.获取token信息：同步请求cookie中获取，异步请求头信息中获取
                String token = request.getHeaders().getFirst("token");
                if (StringUtils.isEmpty(token)){
                    //请求头中获取不到，在cookie中获取
                    MultiValueMap<String, HttpCookie> cookies = request.getCookies();
                    if (!CollectionUtils.isEmpty(cookies) || cookies.containsKey(jwtProperties.getCookieName())){
                        token = cookies.getFirst(jwtProperties.getCookieName()).getValue();
                    }
                }

                //3.判断token是否为空，为空则直接拦截
                if (StringUtils.isEmpty(token)){
                    // 重定向到登录
                    // 303状态码表示由于请求对应的资源存在着另一个URI，应使用重定向获取请求的资源
                    return redirect(request, response);
                }

                try {
                    //4.校验token，有异常拦截
                    Map<String, Object> map = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

                    //5.校验ip
                    String curIp = IpUtil.getIpAddressAtGateway(request);
                    String ip = map.get("ip").toString();
                    if (!StringUtils.equals(curIp,ip)){
                        // 重定向到登录页面
                        redirect(request, response);
                    }

                    //6.传递登录信息给后续的服务，不需要再次解析jwt
                    // 将userId转变成request对象。mutate：转变的意思
                    request.mutate().header("userId",map.get("userId").toString()).build();
                    //将新的request对象转变成exchange对象
                    exchange.mutate().request(request).build();

                } catch (Exception e) {
                    log.error("token解析异常");
                    e.printStackTrace();
                    //重定向到登陆
                    redirect(request, response);
                }

                return chain.filter(exchange);
            }

        };
    }

    public Mono<Void> redirect(ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println("被网关局部过滤器鉴权拦截，转发至登陆页面");
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set(HttpHeaders.LOCATION,"http://sso.gmall.com/toLogin?returnUrl=" + request.getURI());
        return response.setComplete();
    }

    /**
     * 指定字段顺序
     * 可以通过不同的字段分别读取：/toLogin.html,/login
     * 在这里希望通过一个集合字段读取所有的路径
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("pathes");
    }

    /**
     * 指定读取字段的结果集类型
     * 默认通过map的方式，把配置读取到不同字段
     *  例如：/toLogin.html,/login
     *      由于只指定了一个字段，只能接收/toLogin.html
     */
    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST;
    }

    @Data
    @ToString
    public static class KeyValueConfig{

        private List<String> pathes;

    }

}
