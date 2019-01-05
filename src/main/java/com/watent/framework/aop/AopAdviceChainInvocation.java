package com.watent.framework.aop;

import com.watent.framework.aop.advice.AfterAdvice;
import com.watent.framework.aop.advice.AroundAdvice;
import com.watent.framework.aop.advice.BeforeAdvice;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AOP通知链式调用
 *
 * @author Dylan
 */
public class AopAdviceChainInvocation {

    private static Method invokeMethod;

    static {
        try {
            invokeMethod = AopAdviceChainInvocation.class.getMethod("invoke", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Object proxy;
    private Object target;
    private Method method;
    private Object[] args;
    private List<Object> advisors;

    public AopAdviceChainInvocation(Object proxy, Object target, Method method, Object[] args, List<Object> advisors) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.advisors = advisors;
    }

    private int i = 0;

    public Object invoke() throws Throwable {

        if (i < advisors.size()) {
            Object advisor = advisors.get(i++);
            if (advisor instanceof BeforeAdvice) {
                // 执行前置增强
                ((BeforeAdvice) advisor).before(method, args, target);
            } else if (advisor instanceof AroundAdvice) {
                // 执行环绕增强和异常处理增强。注意这里给入的method 和 对象 是invoke方法和链对象
                ((AroundAdvice) advisor).invoke(invokeMethod, args, this);
            } else if (advisor instanceof AfterAdvice) {
                // 当是后置增强时，先得得到结果，再执行后置增强逻辑
                Object returnValue = this.invoke();
                ((AfterAdvice) advisor).after(returnValue, method, args, target);
                return returnValue;
            }
            return this.invoke();
        }
        return method.invoke(target, args);
    }
}
