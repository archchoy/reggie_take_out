package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request 响应给浏览器用户登录的Session信息
     * @param employee 登录请求的员工信息
     * @return 返回登录的员工信息
     */
    @PostMapping("login")
    public CommonResult<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        return employeeService.login(request, employee);
    }


    /**
     * 注销登录
     *
     * @param request 通过 HttpServletRequest 清除已登录的员工信息
     */
    @PostMapping("logout")
    public CommonResult<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }

    /**
     * 新增员工
     * @param request 通过HttpServletRequest获取Session对象来获取进行添加操作员工的ID
     * @param employee 新增的员工信息
     */
    @PostMapping
    public CommonResult<String> save(HttpServletRequest request, @RequestBody Employee employee){
        return employeeService.saveEmployee(request,employee);
    }

    /**
     * 分页查询Employee
     * @param page 当前页码
     * @param pageSize 每页条目数
     * @param name 条件查询
     * @return 返回查到的Page对象
     */
    @GetMapping("page")
    public CommonResult<Page<Employee>> page(Integer page, Integer pageSize, String name){
        return employeeService.pageEmployee(page,pageSize,name);
    }

    /**
     * 修改员工信息
     */
    @PutMapping
    public CommonResult<String> update(HttpServletRequest request, @RequestBody Employee employee){
        return employeeService.updateEmployee(request,employee);
    }

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return 员工信息
     */
    @GetMapping("/{id}")
    public CommonResult<Employee> getEmployeeById(@PathVariable("id") Long id){
        return employeeService.getEmployeeById(id);
    }



}
