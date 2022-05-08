package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.exception.CustomException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private DishService dishService;
    @Resource
    private SetmealService setmealService;


    /**
     * 添加菜品分类
     *
     * @param category 分类信息
     */
    @Override
    public String saveCategory(Category category) {
        baseMapper.insert(category);
        return "添加菜品成功";
    }

    /**
     * 分页查询分类信息
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 分页信息
     */
    @Override
    public Page<Category> pageCategory(Integer page, Integer pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        baseMapper.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }

    /**
     * 通过ID删除分类类
     *
     * @param ids 要删除的菜品的ID
     */
    @Override
    public String delete(Long ids) {
        // 判断分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        if (dishService.count(dishLambdaQueryWrapper)>0){
            // 该分类下有菜品信息 不能删除  抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        if (setmealService.count(setmealLambdaQueryWrapper)>0){
            // 该分类下有套餐信息 不能删除  抛出业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }
        baseMapper.deleteById(ids);
            return "删除分类成功";
    }

    /**
     * 修改分类信息
     * @param category 分类信息
     */
    @Override
    public String updateCategory(Category category) {
        baseMapper.updateById(category);
        return"修改成功";
    }

    @Override
    public List<Category> listType(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }
}
