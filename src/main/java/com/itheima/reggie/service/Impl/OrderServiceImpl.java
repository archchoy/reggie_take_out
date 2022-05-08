package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.exception.CustomException;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;


    @Override
    public CommonResult<Page<Orders>> pageUserOrder(Integer page, Integer pageSize) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        baseMapper.selectPage(pageInfo,queryWrapper);
        return CommonResult.success(pageInfo);
    }

    @Override
    public CommonResult<Page<Orders>> pageAllOrders(Integer page, Integer pageSize) {
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        return CommonResult.success(baseMapper.selectPage(ordersPage,null));
    }

    @Override
    public CommonResult<String> updateOrdersStatus(Orders orders) {
        baseMapper.updateById(orders);
        return CommonResult.success("派送成功");
    }

    @Override
    public CommonResult<String> saveOrders(Orders orders) {
        // 如果用户购物车为空将不能下单
        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartWrapper);
        if (shoppingCartList == null || shoppingCartList.size() ==0){
            throw new CustomException("购物车为空, 不能下单");
        }
        // 收件地址为为空不能下单
        if (addressBookService.getById(orders.getAddressBookId()) == null){
            throw new CustomException("请先添加收件地址");
        }
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getPhone,userId);
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);
        orderDetailService.saveBatch(orderDetails);
        shoppingCartService.remove(shoppingCartWrapper);
        return CommonResult.success("下单成功");

    }
}
