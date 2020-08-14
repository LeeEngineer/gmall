package com.atguigu.gmall.cart.service;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cart.feign.GmallPmsClient;
import com.atguigu.gmall.cart.feign.GmallSmsClient;
import com.atguigu.gmall.cart.feign.GmallWmsClient;
import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.mapper.CartMapper;
import com.atguigu.gmall.cart.pojo.Cart;
import com.atguigu.gmall.cart.pojo.UserInfo;
import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lee_engineer
 * @create 2020-08-07 18:17
 */
@Service
public class CartService {

    public static final String PRE_KEY = "cart:info:";
    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GmallWmsClient wmsClient;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartAsyncService cartAsyncService;

    public Cart queryCartBySkuId(Long skuId) {

        //获取用户登陆信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String userId = null;
        if (userInfo.getUserId() != null) {
            //用户已登陆，使用用户id
            userId = userInfo.getUserId().toString();
        } else {
            //用户未登录，使用userKey
            userId = userInfo.getUserKey();
        }
        String userKey = PRE_KEY + userId;
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(userKey);
        if (hashOps.hasKey(skuId.toString())) {
            String cartJson = hashOps.get(skuId.toString()).toString();
            return JSON.parseObject(cartJson, Cart.class);
        } else {
            throw new RuntimeException("用户不存在对应的购物车记录");
        }
    }

    public void addCart(Cart cart) {

        //获取用户登陆信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String userId = null;
        if (userInfo.getUserId() != null) {
            //用户已登陆，使用用户id
            userId = userInfo.getUserId().toString();
        } else {
            //用户未登录，使用userKey
            userId = userInfo.getUserKey();
        }
        String userKey = PRE_KEY + userId;
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(userKey);
        //判断用户购物车是否已有该商品
        String skuId = cart.getSkuId().toString();
        BigDecimal count = cart.getCount();
        if (hashOps.hasKey(skuId)) {
            //已存在，增加数量
            String cartJson = hashOps.get(skuId).toString();
            cart = JSON.parseObject(cartJson, Cart.class);
            cart.setCount(cart.getCount().add(count));
            //存入mysql数据库
            cartAsyncService.updateCart(userId,cart);

        } else {
            //不存在，直接存入redis
            cart.setUserId(userId);
            cart.setCheck(true);
            SkuEntity skuEntity = pmsClient.querySkuById(cart.getSkuId()).getData();
            if (skuEntity != null) {
                cart.setDefaultImage(skuEntity.getDefaultImage());
                cart.setTitle(skuEntity.getTitle());
                cart.setPrice(skuEntity.getPrice());
            }

            List<WareSkuEntity> wareSkuEntities = wmsClient.getWareSkuBySkuId(cart.getSkuId()).getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                cart.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
            }

            List<ItemSaleVo> itemSaleVos = smsClient.getItemSaleInfoBySkuId(cart.getSkuId()).getData();
            cart.setSaleAttrs(JSON.toJSONString(itemSaleVos));

            List<SkuAttrValueEntity> skuAttrValueEntities = pmsClient.querySearchSkuAttrValueBySkuId(cart.getSkuId()).getData();
            cart.setSaleAttrs(JSON.toJSONString(skuAttrValueEntities));
            // 保存数据到mysql数据库
            cartAsyncService.addCart(userId,cart);

        }
        hashOps.put(skuId, JSON.toJSONString(cart));
    }

    @Async
    public String method1() {

        System.out.println("method1开始执行");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method1结束执行");
        return "method1正常返回";

    }

    @Async
    public String method2() {

        System.out.println("method2开始执行");
        try {
            Thread.sleep(5000);
            int i = 1 / 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method2结束执行");
        return "method2正常返回";

    }

    public List<Cart> queryCarts() {

        //获取用户登陆信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String userKey = userInfo.getUserKey();
        String unLoginKey = PRE_KEY + userKey;
        BoundHashOperations<String, Object, Object> unLoginHashOps = redisTemplate.boundHashOps(unLoginKey);
        // 获取未登录的购物车集合
        List<Object> unLoginCartJsons = unLoginHashOps.values();
        List<Cart> unLoginCarts = null;
        if (!CollectionUtils.isEmpty(unLoginCartJsons)){
            unLoginCarts = unLoginCartJsons.stream().map(cartJson -> JSON.parseObject(cartJson.toString(),Cart.class)).collect(Collectors.toList());
        }

        //判断登录状态
        Long userId = userInfo.getUserId();
        if (userId == null){
            return unLoginCarts;
        }

        //已登陆，合并购物车
        String loginKey = PRE_KEY + userId;
        BoundHashOperations<String, Object, Object> loginHashOps = redisTemplate.boundHashOps(loginKey);
        if (!CollectionUtils.isEmpty(unLoginCarts)){

            unLoginCarts.forEach(cart -> {
                BigDecimal count = cart.getCount();
                if (loginHashOps.hasKey(cart.getSkuId().toString())){
                    String cartJson = loginHashOps.get(cart.getSkuId().toString()).toString();
                    cart = JSON.parseObject(cartJson, Cart.class);
                    cart.setCount(cart.getCount().add(count));
                    // 更新redis和mysql中数量
                    cartAsyncService.updateCart(userId.toString(),cart);
                }else {
                    cart.setUserId(userId.toString());
                    cartAsyncService.addCart(userId.toString(),cart);
                }
                loginHashOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));
            });

            //删除未登录的购物车
            cartAsyncService.deleteCartByUserId(userKey);
            redisTemplate.delete(unLoginKey);
            // 6.查询登录状态的购物车
            List<Object> loginCartJsons = loginHashOps.values();
            if (!CollectionUtils.isEmpty(loginCartJsons)){
                return loginCartJsons.stream().map(cartJson -> JSON.parseObject(cartJson.toString(), Cart.class)).collect(Collectors.toList());
            }
        }
        return null;
    }


    public void updateNum(Cart cart) {

        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String userId = null;
        if (userInfo.getUserId() != null){
            userId = userInfo.getUserId().toString();
        } else {
            userId = userInfo.getUserKey();
        }
        String key = PRE_KEY + userId;

        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        BigDecimal count = cart.getCount();
        if (hashOps.hasKey(cart.getSkuId().toString())){
            String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
            cart = JSON.parseObject(cartJson, Cart.class);
            cart.setCount(count);

            this.cartAsyncService.updateCart(userId,cart);
            hashOps.put(cart.getSkuId().toString(), JSON.toJSONString(cart));
        }

    }

    public void deleteCart(Long skuId) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String userId = null;
        if (userInfo.getUserId() != null){
            userId = userInfo.getUserId().toString();
        } else {
            userId = userInfo.getUserKey();
        }
        String key = PRE_KEY + userId;

        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        if (hashOps.hasKey(skuId.toString())){
            this.cartAsyncService.deleteCartByUserIdAndSkuId(userId, skuId);
            hashOps.delete(skuId.toString());
        }

    }

    public void asyncCart(Set<String> userIds) {

        userIds.forEach(userId -> {

            //删除数据库中的记录
            cartMapper.delete(new UpdateWrapper<Cart>().eq("user_id",userId));
            //从redis中读取cart记录
            BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(PRE_KEY + userId);
            List<Object> cartJsons = hashOps.values();
            if (!CollectionUtils.isEmpty(cartJsons)){

                cartJsons.forEach(cartJson -> {

                    Cart cart = JSON.parseObject(cartJson.toString(), Cart.class);
                    cartMapper.insert(cart);

                });

            }

        });

    }
}
