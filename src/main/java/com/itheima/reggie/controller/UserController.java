package com.itheima.reggie.controller;


import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.entity.dto.UserDto;
import com.itheima.reggie.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     *  发送验证码接口
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public CommonResult<String> sendCheckCode(@RequestBody User user, HttpSession session) {
        return userService.sendCheckCode(user,session);
    }

    /**
     *
     * @param userDto
     * @param session
     * @return
     */
    @PostMapping("/login")
    public CommonResult<User> login(@RequestBody UserDto userDto, HttpSession session){
        return userService.login(userDto,session);
    }


    @PostMapping("loginout")
    public CommonResult<String> logout(HttpServletRequest request){
        return userService.logout(request);
    }


}
