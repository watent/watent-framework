package com.watent.framework.aop.advice;

import java.lang.reflect.Method;

/**
 * 前置通知(增强)
 */
public interface BeforeAdvice {

    /**
     * 前置通知方法
     *
     * @param method 被通知的方法
     * @param args   被通知的方法参数
     * @param target 被通知的目标对象
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}
