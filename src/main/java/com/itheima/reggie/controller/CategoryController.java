package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 添加菜品分类
     * @param category 分类信息
     */
    @PostMapping
    public CommonResult<String> saveCategory(@RequestBody Category category){
        return CommonResult.success(categoryService.saveCategory(category));
    }

    /**
     * 分页查询分类信息
     * @param page 当前页码
     * @param pageSize 每页条目数
     * @return 分页信息
     */
    @GetMapping("page")
    public CommonResult<Page<Category>> pageCategory(Integer page, Integer pageSize){
        return CommonResult.success(categoryService.pageCategory(page,pageSize));
    }

    /**
     * 通过ID删除分类类
     * @param ids 要删除的菜品的ID
     */
    @DeleteMapping
    public CommonResult<String> delete(Long ids){
        return CommonResult.success(categoryService.delete(ids));
    }

    /**
     * 修改分类信息
     * @param category 分类信息
     */
    @PutMapping
    public CommonResult<String> updateCategory(@RequestBody Category category){
        return CommonResult.success(categoryService.updateCategory(category));
    }

    @GetMapping("list")
    public CommonResult<List<Category>> listType(Category category){
        return CommonResult.success(categoryService.listType(category));
    }
}
