package com.watent.framework.bean;

/**
 * Bean 注册接口
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    BeanDefinition getBeanDefinition(String beanName);

    Boolean containsBeanDefinition(String beanName);
}

