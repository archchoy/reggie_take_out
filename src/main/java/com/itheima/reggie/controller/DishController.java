package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.service.DishService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;


    @PostMapping
    public CommonResult<String> saveDishWithFlavor(@RequestBody DishDto dishDto){
        return dishService.saveDishWithFlavor(dishDto);
    }

    @GetMapping("/page")
    public CommonResult<Page<DishDto>> pageDish(Integer page, Integer pageSize,String name){
        return dishService.pageDish(page,pageSize,name);
    }

    @GetMapping("/{id}")
    public CommonResult<DishDto> getDishWithFlavorById(@PathVariable("id") Long id){
        return dishService.getDishWithFlavorById(id);
    }

    @PostMapping("/status/{status}")
    public CommonResult<String> changeStatus(@PathVariable("status") Integer status, String ids){
        return dishService.changeDishStatus(status,ids);
    }

    @PutMapping
    public CommonResult<String> updateDishWithFlavor(@RequestBody DishDto dishDto){
        return dishService.updateDishWithFlavor(dishDto);
    }

    @DeleteMapping
    public CommonResult<String> deleteDishWithFlavor(String ids){
        return dishService.deleteDishWithFlavor(ids);
    }

    @GetMapping("list")
    public CommonResult<List<DishDto>> list(Dish dish){
        return dishService.listDish(dish);
    }
}
