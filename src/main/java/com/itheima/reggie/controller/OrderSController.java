package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderSController {
    @Resource
    private OrderService orderService;


    @PostMapping("/submit")
    public CommonResult<String> saveOrders(@RequestBody Orders orders){
        return orderService.saveOrders(orders);
    }

    @GetMapping("/page")
    public CommonResult<Page<Orders>> pageOrders( Integer page, Integer pageSize){
        return orderService.pageAllOrders(page,pageSize);
    }

    @GetMapping("/userPage")
    public CommonResult<Page<Orders>> pageUserOrder(Integer page, Integer pageSize){
        return orderService.pageUserOrder(page,pageSize);
    }

    @PutMapping
    public CommonResult<String> updateOrdersStatus(@RequestBody Orders orders) {
        return orderService.updateOrdersStatus(orders);
    }
}
