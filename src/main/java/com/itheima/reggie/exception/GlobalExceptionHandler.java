package com.itheima.reggie.exception;

import com.itheima.reggie.common.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

// 对RestController/Controller进行拦截在发生SQLIntegrityConstraintViolationException异常时执行
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 全局SQLIntegrityConstraintViolationException异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public CommonResult<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        // 判断如果错误信息中包含Duplicate entry
        if (e.getMessage().contains("Duplicate entry")) {
            // 将错误信息分离为数组形式 (错误信息: Duplicate entry 'admin' for key 'idx_username' )
            String[] split = e.getMessage().split(" ");
            // 获取重复的信息并处理
            String msg = split[2] + "已存在";
            return CommonResult.error(msg);  // 返回错误信息
        }
        return CommonResult.error("未知错误");
    }

    /**
     * 在全局异常中捕获自定义异常
     * @param e 异常
     * @return 返回异常信息
     */
    @ExceptionHandler(CustomException.class)
    public CommonResult<String> catchCustomException(CustomException e) {
        // 判断如果错误信息中包含Duplicate entry
        return CommonResult.error(e.getMessage());
    }
}
