package com.watent.framework.bean;

/**
 * 后置处理 观察者接口
 */
public interface BeanPostProcessor {

    /**
     * Bean初始化之前
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Throwable {
        return bean;
    }

    /**
     * Bean初始化之后
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws Throwable {
        return bean;
    }
}
