package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.entity.dto.SetmealDto;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmaelDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Resource
    private SetmealService setmealService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private SetmealDishService setmealDishService;


    /**
     * 保存套餐及菜品信息
     * @param setmealDto 套餐信息
     * @return 添加成功提示
     */
    @Override
    @CacheEvict(value = "SetMealCache",allEntries = true)
    @Transactional
    public CommonResult<String> saveSetmealDish(SetmealDto setmealDto) {
        // 保存套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal,"setmealDishes");
        setmealService.save(setmeal);
        // 给套餐的菜品设置套餐的Id 使菜品和套餐关联
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes().stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());
        // 批量保存套餐菜品关系
        setmealDishService.saveBatch(setmealDishList);

        return CommonResult.success("添加成功");
    }



    /**
     * 分页查询套餐信息
     * @param page 当前页码
     * @param pageSize 每页显示条数
     * @return Page<SetmealDto>
     */
    @Override
    @Transactional
    public CommonResult<Page<SetmealDto>> pageSetmealDish(Integer page, Integer pageSize) {
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        setmealService.page(pageInfo);
        Page<SetmealDto> dtoPage = new Page<>();
        // 将基本分页信息复制到dto中
        BeanUtils.copyProperties(pageInfo,dtoPage);
        // 获取分页中查到的数据
        List<Setmeal> records = pageInfo.getRecords();
        // 将每个SetMeal中的数据复制到SetMealDto 再根据
        List<SetmealDto> setmealDtoList = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());
        // 将分页数据设置到DTO中
        dtoPage.setRecords(setmealDtoList);

        return CommonResult.success(dtoPage);
    }

    /**
     * 根据ID回显套餐菜品信息
     * @param id 套餐ID
     * @return 套餐菜品DTO对象
     */
    @Override
    @Transactional
    public CommonResult<SetmealDto> getSetmealDishById(Long id) {
        // 获取套餐信息封装到setmealDishDto
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        // 获取基本套餐菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        // 将信息封装到setmealDishDto中
        setmealDto.setSetmealDishes(setmealDishList);

        return CommonResult.success(setmealDto);
    }

    /**
     * 修改套餐菜品信息
     * @param setmealDto 修改的信息
     * @return 修改成功提示
     */
    @Override
    @Transactional
    public CommonResult<String> updateSetmealDishInfo(SetmealDto setmealDto) {
        // 一, 修改SetMeal 信息
        // 1.将DTO中的基本信息提取到SetMeal中
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto,setmeal);
        // 2.修改SetMeal信息
        setmealService.updateById(setmeal);
        // 二, SetMealDish 信息
        // 1.删除SetMealDish信息
        LambdaUpdateWrapper<SetmealDish> removeWrapper = new LambdaUpdateWrapper<>();
        removeWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        setmealDishService.remove(removeWrapper);
        // 2.重新获取SetMealDish信息,修改SetmealId
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes().stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());
        // 3.修改SetMealDish信息
        setmealDishService.saveBatch(setmealDishList);
        return CommonResult.success("修改成功");
    }

    /**
     * 修改套餐的起售状态
     * @param status 状态
     * @param IDS_STRING 套餐ID
     * @return 改变状态成功提示
     */
    @Override
    public CommonResult<String> changeSetmealStatus(Integer status,String IDS_STRING) {
        List<Long> ID_LONG_LIST = new ArrayList<>();
        for (String ID: IDS_STRING.split(",")){
            ID_LONG_LIST.add(Long.parseLong(ID));
        }
        LambdaUpdateWrapper<Setmeal> changeStatesWrapper = new LambdaUpdateWrapper<>();
        changeStatesWrapper.set(Setmeal::getStatus,status).in(Setmeal::getId,ID_LONG_LIST);
        setmealService.update(changeStatesWrapper);
        return CommonResult.success("修改成功");
    }

    @Override
    // 删除SetMealCache分类下的所有数据
    @CacheEvict(value = "SetMealCache",allEntries = true)
    @Transactional
    public CommonResult<String> removeSetmeal(String IDS_STRING) {
        List<Long> ID_LONG_LIST = new ArrayList<>();
        for (String ID: IDS_STRING.split(",")){
            ID_LONG_LIST.add(Long.parseLong(ID));
        }
        setmealService.listByIds(ID_LONG_LIST).forEach((item) ->{
            if (item.getStatus() ==0) {
                // 删除套餐信息
                LambdaUpdateWrapper<Setmeal> removeSetmealWrapper = new LambdaUpdateWrapper<>();
                removeSetmealWrapper.in(Setmeal::getId, item.getId());
                setmealService.remove(removeSetmealWrapper);

                // 删除套餐菜品信息
                LambdaUpdateWrapper<SetmealDish> removeSetmealDishWrapper = new LambdaUpdateWrapper<>();
                removeSetmealDishWrapper.in(SetmealDish::getSetmealId, item.getId());
                setmealDishService.remove(removeSetmealDishWrapper);
            }
        });
        return CommonResult.success("删除成功");
    }


    @Cacheable(value = "SetMealCache",key = "#setmeal.categoryId + '_' + #setmeal.status",condition = "#result.data!=null")
    @Override
    public CommonResult<List<Setmeal>> listSetmeal(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> listSetmealWrapper = new LambdaQueryWrapper<>();
        listSetmealWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId()).eq(Setmeal::getStatus,setmeal.getStatus());
        return CommonResult.success(setmealService.list(listSetmealWrapper));
    }
}
