package com.watent.framework.bean;

/**
 * 自定义Bean注册异常
 *
 * @author Dylan
 */
public class BeanDefinitionRegistryException extends Exception {

    public BeanDefinitionRegistryException(String message) {
        super(message);
    }

    public BeanDefinitionRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
