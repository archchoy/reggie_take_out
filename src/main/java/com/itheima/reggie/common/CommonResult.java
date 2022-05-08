package com.itheima.reggie.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果
 * @param <T>
 */
@Data
public class CommonResult<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> CommonResult<T> success(T object) {
        CommonResult<T> commonResult = new CommonResult<T>();
        commonResult.data = object;
        commonResult.code = 1;
        return commonResult;
    }

    public static <T> CommonResult<T> error(String msg) {
        CommonResult commonResult = new CommonResult();
        commonResult.msg = msg;
        commonResult.code = 0;
        return commonResult;
    }

    public CommonResult<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
