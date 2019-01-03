package com.watent.framework.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 预先调用单实例初始化 单例安全
 *
 * @author Dylan
 */
public class PreBuildBeanFactory extends DefaultBeanFactory {

    private Logger logger = LoggerFactory.getLogger(PreBuildBeanFactory.class);

    private List<String> beanNames = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
        super.registerBeanDefinition(beanName, beanDefinition);
        synchronized (beanNames) {
            beanNames.add(beanName);
        }
    }

    public void preInstantiateSingletons() throws Throwable {

        synchronized (beanNames) {
            for (String beanName : beanNames) {
                BeanDefinition bd = getBeanDefinition(beanName);
                if (bd.isSingleton()) {
                    doGetBean(beanName);
                    if (logger.isDebugEnabled()) {
                        logger.debug("preInstantiate: name=" + beanName + " " + bd);
                    }
                }

            }
        }
    }
}
