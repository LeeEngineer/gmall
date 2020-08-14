package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.mapper.CartMapper;
import com.atguigu.gmall.cart.pojo.Cart;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Lee_engineer
 * @create 2020-08-07 20:39
 */
@Service
public class CartAsyncService {

    @Autowired
    private CartMapper cartMapper;

    @Async
    public void addCart(String userId,Cart cart) {
        int i = 10 / 0;
        cartMapper.insert(cart);
    }
    @Async
    public void updateCart(String userId,Cart cart) {

        cartMapper.update(cart, new UpdateWrapper<Cart>().eq("user_id", userId).eq("sku_id",cart.getSkuId()));

    }
    @Async
    public void deleteCartByUserId(String userId){
        cartMapper.delete(new UpdateWrapper<Cart>().eq("user_id", userId));
    }
    @Async
    public void deleteCartByUserIdAndSkuId(String userId, Long skuId) {
        this.cartMapper.delete(new UpdateWrapper<Cart>().eq("user_id",userId).eq("sku_id", skuId));
    }
}
