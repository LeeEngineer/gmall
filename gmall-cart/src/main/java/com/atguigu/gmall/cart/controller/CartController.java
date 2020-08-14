package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.pojo.Cart;
import com.atguigu.gmall.cart.pojo.UserInfo;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.bean.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Lee_engineer
 * @create 2020-08-05 20:45
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String addCart(Cart cart){

        cartService.addCart(cart);
        return "redirect:http://cart.gmall.com/addCart?skuId=" + cart.getSkuId();

    }

    @GetMapping("addCart")
    public String queryCartBySkuId(@RequestParam Long skuId, Model model){

        Cart cart = cartService.queryCartBySkuId(skuId);
        model.addAttribute("cart", cart);
        return "addCart";

    }

    @GetMapping("/test")
    @ResponseBody
    public String test() throws ExecutionException, InterruptedException {

        long begin = System.currentTimeMillis();
        System.out.println("程序开始");
//        ListenableFuture<String> method1 = cartService.method1();
//        ListenableFuture<String> method2 = cartService.method2();

//        method1.addCallback(t -> System.out.println(Thread.currentThread().getName() + ":" + t),e -> System.out.println(e.getMessage()));
//        method2.addCallback(t -> System.out.println(Thread.currentThread().getName() + ":" + t),e -> System.out.println(e.getMessage()));

        System.out.println(cartService.method1());
        System.out.println(cartService.method2());

        System.out.println("程序结束，耗时:" + (System.currentTimeMillis() - begin));
        return "ok";
    }

    @GetMapping("/cart.html")
    public String queryCarts(Model model){

        List<Cart> carts = cartService.queryCarts();
        model.addAttribute("carts", carts);
        return "cart";

    }

    @PostMapping("updateNum")
    @ResponseBody
    public ResponseVo<Object> updateNum(@RequestBody Cart cart){

        this.cartService.updateNum(cart);
        return ResponseVo.ok();
    }

    @PostMapping("deleteCart")
    @ResponseBody
    public ResponseVo<Object> deleteCart(@RequestParam("skuId")Long skuId){
        this.cartService.deleteCart(skuId);
        return ResponseVo.ok();
    }

    @PostMapping("asyncCart")
    @ResponseBody
    public ResponseVo<Object> asyncCart(@RequestBody Set<String> UserIds){

        cartService.asyncCart(UserIds);
        return ResponseVo.ok();

    }

}
