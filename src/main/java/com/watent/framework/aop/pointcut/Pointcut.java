package com.watent.framework.aop.pointcut;

import java.lang.reflect.Method;

/**
 * 切点 决定在哪里执行通知
 * 定义表达式 定义匹配方法
 * 可以有不同的实现 如 正则/AspectJ 表达式
 */
public interface Pointcut {

    /***
     * 匹配类
     */
    boolean matchClass(Class<?> targetClass);

    /**
     * 匹配方法
     */
    boolean matchMethod(Method method, Class<?> targetClass);
}
