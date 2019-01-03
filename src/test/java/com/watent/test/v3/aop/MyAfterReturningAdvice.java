package com.watent.test.v3.aop;

import com.watent.framework.aop.AfterAdvice;

import java.lang.reflect.Method;

public class MyAfterReturningAdvice implements AfterAdvice {

    @Override
    public void after(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println(this + " 对 " + target + " 做了后置增强，得到的返回值=" + returnValue);
    }

}
