package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    CommonResult<Page<Orders>> pageUserOrder(Integer page, Integer pageSize);

    CommonResult<String> saveOrders(Orders orders);

    CommonResult<Page<Orders>> pageAllOrders(Integer page, Integer pageSize);

    CommonResult<String> updateOrdersStatus(Orders orders);
}
