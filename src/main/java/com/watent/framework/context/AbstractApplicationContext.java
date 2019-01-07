package com.watent.framework.context;

import com.watent.framework.bean.BeanFactory;
import com.watent.framework.bean.BeanPostProcessor;
import com.watent.framework.bean.DefaultBeanFactory;

/**
 * @author Dylan
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    protected BeanFactory beanFactory;

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultBeanFactory();
    }

    @Override
    public Object getBean(String beanName) throws Throwable {
        return beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanFactory.registerBeanPostProcessor(beanPostProcessor);
    }
}
