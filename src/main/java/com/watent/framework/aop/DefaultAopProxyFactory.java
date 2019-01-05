package com.watent.framework.aop;

import com.watent.framework.aop.advisor.Advisor;
import com.watent.framework.bean.BeanFactory;

import java.util.List;

/**
 * @author Dylan
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object bean, String beanName, List<Advisor> matchAdvisors, BeanFactory beanFactory) throws Throwable {

        // 判断用 jdk动态代理 还是cglib？
        if (shouldUseJDKDynamicProxy(bean, beanName)) {
            return new JdkDynamicAopProxy(beanName, bean, matchAdvisors, beanFactory);
        } else {
            return new CglibDynamicAopProxy(beanName, bean, matchAdvisors, beanFactory);
        }
    }

    private boolean shouldUseJDKDynamicProxy(Object bean, String beanName) {
        // 如何判断？
        // 这样可以吗：有实现接口就用JDK,没有就用cglib？
        // 请在读spring的源码时看spring中如何来判断的 排除 Closeable 等
        return false;
    }
}
