package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /**
     * 员工登录
     * @param request 响应给浏览器用户登录的Session信息
     * @param employee 登录请求的员工信息
     * @return 返回登录的员工信息
     */
    public CommonResult<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee loginEmployee = baseMapper.selectOne(queryWrapper);
        //3、如果没有查询到则返回登录失败结果
        if ( loginEmployee ==null ){
            return CommonResult.error("用户名或密码错误");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if (!loginEmployee.getPassword().equals(password)){
            return CommonResult.error("用户名或密码错误");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (loginEmployee.getStatus() == 0){
            return CommonResult.error("该账号已被禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee" ,loginEmployee.getId());
        return CommonResult.success(loginEmployee);
    }


    /**
     * 注销登录
     * @param request 通过 HttpServletRequest 清除已登录的员工信息
     */
public CommonResult<String> logout(HttpServletRequest request){
    // 清除Session中当前登录用户的id
    request.getSession().removeAttribute("employee");
    return CommonResult.success("退出成功");
}

    /**
     * 新增员工
     * @param request 通过HttpServletRequest获取Session对象来获取进行添加操作员工的ID
     * @param employee 新增的员工信息
     */
public CommonResult<String> saveEmployee(HttpServletRequest request, @RequestBody Employee employee){
    // 为员工设置默认密码为 123456
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    baseMapper.insert(employee);
    return CommonResult.success("新增员工成功");
}

    /**
     * 分页查询Employee
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     条件查询
     * @return 返回查到的Page对象
     */
    @Override
    public CommonResult<Page<Employee>> pageEmployee(Integer page, Integer pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 封装查询条件 当查询条件不为空 就根据name 查询 name字段
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        // 降序排列
        queryWrapper.orderByDesc(Employee::getCreateTime);
        baseMapper.selectPage(pageInfo,queryWrapper);
        // 返回查询到的Page对象
        return CommonResult.success(pageInfo);
    }

    /**
     * 修改员工信息
     */
    @Override
    public CommonResult<String> updateEmployee(HttpServletRequest request,Employee employee) {
        baseMapper.updateById(employee);
        return CommonResult.success("员工信息修改成功");
    }

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     */
    public CommonResult<Employee> getEmployeeById(Long id){
        if (baseMapper.selectById(id) != null){
            return CommonResult.success(baseMapper.selectById(id));
        }
        return CommonResult.error("没有查询到该员工信息");
    }
}
