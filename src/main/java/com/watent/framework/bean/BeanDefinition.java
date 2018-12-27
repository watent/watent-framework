package com.watent.framework.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * Bean 定义接口
 * <p>
 * 创建Bean
 * 1. new
 * 类名
 * 2. 静态工厂方法
 * 工厂类名     工厂方法名
 * 3. 成员工厂方法
 * 工厂Bean名   工厂方法名
 *
 * 工厂类名 <==> 类名
 */
public interface BeanDefinition {

    /**
     * 单例
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * 多例
     */
    String SCOPE_PROTOTYPE = "prototype";

    Class<?> getBeanClass();

    String getFactoryBeanName();

    String getFactoryMethodName();

    String getScope();

    String getInitMethodName();

    String getDestroyMethodName();

    /**
     * 是否单例
     */
    boolean isSingleton();

    /**
     * 是否多例
     */
    boolean isPrototype();

    /**
     * 校验
     */
    default boolean validate() {

        //Class 没指定  同时 工厂Bean 或 工厂方法也没指定 不合法 无法创建 Bean
        if (null == this.getBeanClass()) {
            if (null == this.getFactoryBeanName() || StringUtils.isBlank(getFactoryMethodName())) {
                return false;
            }
        }
        //指定类 又指定 工厂Bean 不合法
        if (null != this.getBeanClass() && StringUtils.isNotBlank(getFactoryBeanName())) {
            return false;
        }

        return true;
    }

}
