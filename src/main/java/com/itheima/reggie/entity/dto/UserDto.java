package com.itheima.reggie.entity.dto;

import com.itheima.reggie.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String code;
}
