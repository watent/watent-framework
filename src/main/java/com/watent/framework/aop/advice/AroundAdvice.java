package com.watent.framework.aop.advice;

import java.lang.reflect.Method;

/**
 * 环绕通知
 */
public interface AroundAdvice extends Advice {

    /**
     * 方法环绕通知(前置 后置) 异常处理通知 方法实现中需调用目标方法
     *
     * @param method 被通知方法
     * @param args   被通知方法参数
     * @param target 被通知方法目标对象
     * @return 方法返回值
     * @throws Throwable t
     */
    Object invoke(Method method, Object[] args, Object target) throws Throwable;

}
