package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.entity.dto.SetmealDto;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    /**
     * 分页套餐信息
     * @param page 当前页码
     * @param pageSize 每页显示条数
     * @return SetmealDto
     */
    CommonResult<Page<SetmealDto>> pageSetmealDish(Integer page, Integer pageSize);

    /**
     * 添加套餐
     * @param setmealDto 套餐信息
     * @return 添加套餐成功提示
     */
    CommonResult<String> saveSetmealDish(SetmealDto setmealDto);

    /**
     * 根据ID回显套餐菜品信息
     * @param id 套餐ID
     * @return 套餐菜品DTO对象
     */
    CommonResult<SetmealDto> getSetmealDishById(Long id);

    /**
     * 修改套餐菜品信息
     * @param setmealDto 套餐和菜品信息DTO
     * @return 修改成功提示
     */
    CommonResult<String> updateSetmealDishInfo(SetmealDto setmealDto);

    /**
     * 改变套餐起售状态
     * @param IDS_STRING 套餐ID
     * @return 改变成功提示
     */
    CommonResult<String> changeSetmealStatus(Integer status,String IDS_STRING);

    /**
     * 删除套餐
     * @param ids 套餐ID
     * @return 删除成功提示
     */
    CommonResult<String> removeSetmeal(String IDS_STRING);

    CommonResult<List<Setmeal>> listSetmeal(Long categoryId, Integer status);
}
