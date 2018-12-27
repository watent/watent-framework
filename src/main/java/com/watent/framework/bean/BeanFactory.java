package com.watent.framework.bean;

/**
 * Bean工厂
 */
public interface BeanFactory {

    /**
     * get bean
     *
     * @param beanName bean name
     * @return Bean Instance
     */
    Object getBean(String beanName) throws Exception;


}
