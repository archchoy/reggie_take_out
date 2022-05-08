package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 添加菜品分类
     * @param category 分类信息
     */
    String saveCategory(Category category);


    /**
     * 分页查询分类信息
     * @param page 当前页码
     * @param pageSize 每页条目数
     * @return 分页信息
     */
    Page<Category> pageCategory(Integer page, Integer pageSize);


    /**
     * 通过ID删除分类类
     * @param id 要删除的菜品的ID
     */
    String delete(Long id);

    /**
     * 修改分类信息
     * @param category 分类信息
     */
    String updateCategory(Category category);

    List<Category> listType(Category category);
}
