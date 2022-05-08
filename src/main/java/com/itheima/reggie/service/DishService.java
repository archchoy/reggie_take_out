package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {
    /**
     * 保存包含口味的菜品信息
     * @param dishDto 菜品信息和口味信息的封装
     * @return 返回保存成功信息
     */
    CommonResult<String> saveDishWithFlavor(DishDto dishDto);

    /**
     * 分页查询菜品信息
     * @param page 当前页码
     * @param pageSize 每页条数
     * @param name 根据名称条件查询
     * @return 封装了DishDto的Page对象
     */
    CommonResult<Page<DishDto>> pageDish(Integer page, Integer pageSize, String name);

    /**
     * 根据ID查询菜品和口味信息
     * @param id 菜品ID
     * @return 菜品和口味信息的DishDto
     */
    CommonResult<DishDto> getDishWithFlavorById(Long id);

    /**
     * 修改菜品和口味信息
     * @param dishDto 菜品信息和口味
     * @return 修改成功结果
     */
    CommonResult<String> updateDishWithFlavor(DishDto dishDto);

    /**
     * 修改菜品售卖状态
     * @param status 菜品是否起售
     * @param IDS_STRING 菜品Id
     * @return 售卖状态修改结果
     */
    CommonResult<String> changeDishStatus(Integer status, String IDS_STRING);

    /**
     * 删除菜品
     * @param IDS_STRING 菜品的 ID (一个或多个)
     * @return 删除成功提示
     */
    CommonResult<String> deleteDishWithFlavor(String IDS_STRING);

    /**
     * 根据categoryId查询菜品信息列表
     * @return 菜品列表
     */
    CommonResult<List<DishDto>> listDish(Dish dish);
}
