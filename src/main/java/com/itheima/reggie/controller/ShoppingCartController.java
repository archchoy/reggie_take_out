package com.itheima.reggie.controller;

import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public CommonResult<List<ShoppingCart>> listShoppingCart(){

return shoppingCartService.listShoppingCart();
    }

    @PostMapping("/add")
    public CommonResult<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.addShoppingCart(shoppingCart);
    }

    @PostMapping("/sub")
    public CommonResult<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.subShoppingCart(shoppingCart);
    }

    @DeleteMapping("/clean")
    public CommonResult<String> cleanShoppingCart(){
        return shoppingCartService.cleanShoppingCart();
    }

}
