package com.watent.framework.aop;

import java.lang.reflect.Method;

/**
 * 后置通知(增强)
 */
public interface AfterAdvice extends Advice {

    /**
     * 后置通知方法
     *
     * @param returnValue 返回值
     * @param method      被通知的方法
     * @param args        被通知的方法参数
     * @param target      被通知的目标对象
     */
    void after(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
}
