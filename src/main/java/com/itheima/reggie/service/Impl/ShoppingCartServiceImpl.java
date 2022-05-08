package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public CommonResult<List<ShoppingCart>> listShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> listQueryWrapper = new LambdaQueryWrapper<>();
        listQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        return CommonResult.success(baseMapper.selectList(listQueryWrapper));
    }

    @Override
    public CommonResult<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<ShoppingCart> isExistsQueryWrapper = new LambdaQueryWrapper<>();
        isExistsQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if (shoppingCart.getDishId() != null){
            isExistsQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else if (shoppingCart.getSetmealId() != null){
            isExistsQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart existShoppingCart = baseMapper.selectOne(isExistsQueryWrapper);
        if ( existShoppingCart != null){
            existShoppingCart.setNumber(existShoppingCart.getNumber() + 1);
            baseMapper.updateById(existShoppingCart);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            baseMapper.insert(shoppingCart);
            existShoppingCart = shoppingCart;
        }
        return CommonResult.success(existShoppingCart);
    }

    @Override
    public CommonResult<ShoppingCart> subShoppingCart(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> subWrapper = new LambdaQueryWrapper<>();
        subWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        subWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        ShoppingCart updateShoppingCart = baseMapper.selectOne(subWrapper);
        if (updateShoppingCart.getNumber()>1){
            updateShoppingCart.setNumber(updateShoppingCart.getNumber()-1);
            baseMapper.updateById(updateShoppingCart);
            return CommonResult.success(updateShoppingCart);
        }else{
            baseMapper.delete(subWrapper);
            ShoppingCart no = new ShoppingCart();
            no.setNumber(0);
            return CommonResult.success(no);
        }
    }

    public CommonResult<String> cleanShoppingCart(){
        LambdaQueryWrapper<ShoppingCart> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        baseMapper.delete(deleteWrapper);
        return CommonResult.success("删除成功");
    }

}
