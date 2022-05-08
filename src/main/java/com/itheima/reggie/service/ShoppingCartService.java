package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    CommonResult<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart);

    CommonResult<ShoppingCart> subShoppingCart(ShoppingCart shoppingCart);

    CommonResult<List<ShoppingCart>> listShoppingCart();


    CommonResult<String> cleanShoppingCart();

}
