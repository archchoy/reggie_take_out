package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 保存菜品及其口味
     *
     * @param dishDto 口味
     */
    @Transactional // 开启事务
    @Override
    public CommonResult<String> saveDishWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        // 获取菜品的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
//        flavors.forEach(dishFlavor -> {
//            // 将口味信息与菜品绑定
//            dishFlavor.setDishId(dishDto.getId());
//        });
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return CommonResult.success("菜品添加成功");
    }

    /**
     * 分页菜品
     *
     * @param page     当前页码
     * @param pageSize 每页条数
     * @param name     分页条件
     */
    @Override
    public CommonResult<Page<DishDto>> pageDish(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> pageDishDto = new Page<>();
        // 当name不为空  根据其封装分页的条件查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getCreateTime);
        baseMapper.selectPage(pageInfo, queryWrapper);
        // 将分页相关的基本信息copy到 DTO 中
        BeanUtils.copyProperties(pageInfo, pageDishDto, "records");
        // 取出分页查出的数据 records
        List<Dish> records = pageInfo.getRecords();
        // 遍历出records 复制到DTO,再查出分类名称给DTO中的属性复制并入集合中
        List<DishDto> dishDtoRecords = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            return dishDto;
        }).collect(Collectors.toList());
        // 数据信息给DTO
        pageDishDto.setRecords(dishDtoRecords);
        return CommonResult.success(pageDishDto);
    }

    /**
     * 根据ID获取带有口味的菜品
     *
     * @param id 菜品ID
     * @return 菜品DTO
     */
    @Override
    public CommonResult<DishDto> getDishWithFlavorById(Long id) {
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(baseMapper.selectById(id), dishDto);
        dishDto.setFlavors(dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(id != null, DishFlavor::getDishId, id)));
        return CommonResult.success(dishDto);
    }

    /**
     * 修改菜品和其口味信息
     *
     * @param dishDto 菜品信息和口味
     * @return 修改成功提示
     */
    @Transactional
    @Override
    public CommonResult<String> updateDishWithFlavor(DishDto dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        baseMapper.updateById(dish);
        // 先清除对应的口味数据
        dishFlavorService.remove(new LambdaUpdateWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishDto.getId()));
        // 挨个修改口味 的 dish_id 使dish和口味绑定
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        // 批量保存修改后的口味数据
        dishFlavorService.saveBatch(flavors);

        // 删除redis中的缓存数据
        Set keys = redisTemplate.keys("dish_*");    // 匹配所有以dish_开头的key
        redisTemplate.delete(keys);   // 删除缓存中的菜品信息数据
        return CommonResult.success("修改菜品信息成功");
    }

    /**
     * 根据Id改变菜品的售卖状态
     *
     * @param status     菜品是否起售
     * @param IDS_STRING 一个或多个ID
     * @return 修改成功状态
     */
    @Override
    public CommonResult<String> changeDishStatus(Integer status, String IDS_STRING) {
        List<Long> ID_LONG_LIST = new ArrayList<>();
        for (String ID : IDS_STRING.split(",")) {
            ID_LONG_LIST.add(Long.parseLong(ID));
        }
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Dish::getId, ID_LONG_LIST).set(Dish::getStatus, status);
        baseMapper.update(null, wrapper);
        return CommonResult.success("状态更新成功");
    }

    /**
     * 通过ID删除一个或多个
     *
     * @param IDS_STRING 菜品的 ID (一个或多个)
     * @return 删除成功状态
     */
    @Override
    @Transactional
    public CommonResult<String> deleteDishWithFlavor(String IDS_STRING) {
        List<Long> ID_LONG_LIST = new ArrayList<>();
        // 取出所有ID
        for (String ID : IDS_STRING.split(",")) {
            ID_LONG_LIST.add(Long.parseLong(ID));
        }
        baseMapper.selectBatchIds(ID_LONG_LIST).forEach((item) -> {
            if (item.getStatus() == 0) {
                // 删除菜品
                LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.in(Dish::getId, item.getId());
                baseMapper.delete(updateWrapper);
                // 删除菜品关联的口味信息
                LambdaUpdateWrapper<DishFlavor> wrapper = new LambdaUpdateWrapper<>();
                wrapper.in(DishFlavor::getDishId, item.getId());
                dishFlavorService.remove(wrapper);
            }
        });
        return CommonResult.success("删除成功");
    }


    @Override
    public CommonResult<List<DishDto>> listDish(Dish dish) {
        List<DishDto> dishDtoList = null;
        // 手动拼接存入redis的Key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>)redisTemplate.opsForValue().get(key);
        if (dishDtoList != null){// 如果缓存中的dishDtoList不为空就直接返回
            return CommonResult.success(dishDtoList);
        }
        // Redis中不存在dishDtoList就从数据库中查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, dish.getCategoryId()).eq(Dish::getStatus,dish.getStatus());
        List<Dish> dishList = baseMapper.selectList(dishLambdaQueryWrapper);
        dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            // 查询菜品对应的口味数据
            LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
            flavorWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavorList = dishFlavorService.list(flavorWrapper);
            dishDto.setFlavors(flavorList);
            return dishDto;
        }).collect(Collectors.toList());

        // 从数据库查到数据后存到Redis中
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return CommonResult.success(dishDtoList);
    }
}
