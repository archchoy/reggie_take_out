package com.itheima.reggie.exception;

public class CustomException extends RuntimeException{
    public CustomException(String errorMsg){
        super(errorMsg);
    }
}
