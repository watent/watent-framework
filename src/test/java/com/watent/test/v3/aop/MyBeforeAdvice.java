package com.watent.test.v3.aop;

import com.watent.framework.aop.BeforeAdvice;

import java.lang.reflect.Method;

public class MyBeforeAdvice implements BeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(this + " 对 " + target + " 进行了前置增强！");
    }

}
