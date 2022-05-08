package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.dto.SetmealDto;
import com.itheima.reggie.service.SetmealDishService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealDishService setmealDishService;



    @PostMapping
    public CommonResult<String> saveSetmealDish(@RequestBody SetmealDto setmealDto){
        return setmealDishService.saveSetmealDish(setmealDto);
    }

    @GetMapping("page")
    public CommonResult<Page<SetmealDto>> pageSetMeal(Integer page, Integer pageSize) {
        return setmealDishService.pageSetmealDish(page,pageSize);
    }

    @GetMapping("/{id}")
    public CommonResult<SetmealDto> getSetmealDishById(@PathVariable("id") Long id){
        return setmealDishService.getSetmealDishById(id);
    }

    @PutMapping
    public CommonResult<String> updateSetmealDishInfo(@RequestBody SetmealDto setmealDto){
        return setmealDishService.updateSetmealDishInfo(setmealDto);
    }

    @PostMapping("status/{status}")
    public CommonResult<String> changeSetmealStatus(@PathVariable("status") Integer status,String ids){
        return setmealDishService.changeSetmealStatus(status,ids);
    }



    @DeleteMapping
    public CommonResult<String> removeSetmeal(String ids){
        return setmealDishService.removeSetmeal(ids);
    }

    @GetMapping("/list")
    public CommonResult<List<Setmeal>> listSetmeal(Setmeal setmeal){
        return setmealDishService.listSetmeal(setmeal);
    }



}
