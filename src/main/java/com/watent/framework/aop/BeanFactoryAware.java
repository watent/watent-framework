package com.watent.framework.aop;

import com.watent.framework.bean.BeanFactory;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory bf);
}
