package com.itheima.reggie.common;

/**
 * 向当前线程设置和获取值的工具类
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    // 在当前线程中设置ID
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    // 获取当前线程的ID
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
