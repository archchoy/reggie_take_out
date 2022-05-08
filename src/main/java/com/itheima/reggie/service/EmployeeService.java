package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;


public interface EmployeeService extends IService<Employee> {

    /**
     * 注销登录
     * @param request 通过 HttpServletRequest 清除已登录的员工信息
     */
    CommonResult<String> logout(HttpServletRequest request);

    /**
     * 新增员工
     * @param request 通过HttpServletRequest获取Session对象来获取进行添加操作员工的ID
     * @param employee 新增的员工信息
     */
    CommonResult<String> saveEmployee(HttpServletRequest request, Employee employee);


    /**
     * 员工登录
     * @param request 响应给浏览器用户登录的Session信息
     * @param employee 登录请求的员工信息
     * @return 返回登录的员工信息
     */
    CommonResult<Employee> login(HttpServletRequest request, Employee employee);
    /**
     * 分页查询Employee
     * @param page 当前页码
     * @param pageSize 每页条目数
     * @param name 条件查询
     * @return 返回查到的Page对象
     */
    CommonResult<Page<Employee>> pageEmployee(Integer page, Integer pageSize, String name);

    /**
     *
     * @param request 获取Session中的进行修改操作的员工ID
     * @param employee 修改后的员工信息
     */
    CommonResult<String> updateEmployee(HttpServletRequest request,Employee employee);

    /**
     * @param id 员工ID
     * @return 员工信息
     */
    CommonResult<Employee> getEmployeeById(Long id);
}
