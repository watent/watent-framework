package com.watent.framework.aop;

import com.watent.framework.aop.advisor.Advisor;
import com.watent.framework.bean.BeanFactory;

import java.util.List;

public interface AopProxyFactory {

    /**
     * 创建代理
     */
    AopProxy createAopProxy(Object bean, String beanName, List<Advisor> matchAdvisors, BeanFactory beanFactory) throws Throwable;

    /**
     * 获取默认
     */
    static AopProxyFactory getDefaultAopProxyFactory() {
        return new DefaultAopProxyFactory();
    }

}
