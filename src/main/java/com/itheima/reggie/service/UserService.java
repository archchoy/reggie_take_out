package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.entity.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    CommonResult<String> sendCheckCode(User user, HttpSession session);

    CommonResult<User> login(UserDto userDto, HttpSession session);

    CommonResult<String> logout(HttpServletRequest request);
}
