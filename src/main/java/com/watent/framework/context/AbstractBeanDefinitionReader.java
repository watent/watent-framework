package com.watent.framework.context;

import com.watent.framework.bean.BeanDefinitionRegistry;

/**
 * @author Dylan
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    protected BeanDefinitionRegistry registry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }



}
