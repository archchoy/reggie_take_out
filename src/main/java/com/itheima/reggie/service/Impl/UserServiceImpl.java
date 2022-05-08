package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.common.SMSUtils;
import com.itheima.reggie.common.ValidateCodeUtils;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.entity.dto.UserDto;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public CommonResult<String> sendCheckCode(User user, HttpSession session) {
        // 如果号码不为空生成4位数验证码
        if (StringUtils.isNotEmpty(user.getPhone())) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            // 将验证码存入 redis 5分钟内有效
            redisTemplate.opsForValue().set("LoginCheckCode",code,5, TimeUnit.MINUTES);
            log.info("登录验证码: " + code);
            // 发送验证码工具类
            SMSUtils.sendCheckCode("测试专用模板", user.getPhone(), code);
            session.setAttribute(user.getPhone(), code);
            return CommonResult.success("验证码已发送,5分钟内有效");
        }
        return CommonResult.error("验证码发送失败");
    }

    @Override
    public CommonResult<User> login(UserDto userDto, HttpSession session) {
        String LoginCheckCode = redisTemplate.opsForValue().get("LoginCheckCode");
        if (LoginCheckCode!=null && LoginCheckCode.equals(userDto.getCode()) ){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,userDto.getPhone());
            User selectUser = baseMapper.selectOne(queryWrapper);
            session.setAttribute("user",Long.parseLong(userDto.getPhone()));
            if (selectUser == null){
                User user = new User();
                user.setPhone(userDto.getPhone());
                user.setId(Long.parseLong(userDto.getPhone()));
                user.setStatus(1);
                baseMapper.insert(user);
            }
            return CommonResult.success(selectUser);
        }
        return CommonResult.error("验证码错误");
    }

    @Override
    public CommonResult<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return CommonResult.success("已注销登录");
    }
}
