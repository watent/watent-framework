package com.watent.framework.aop;

import com.watent.framework.aop.advisor.Advisor;
import com.watent.framework.bean.BeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 实现JDK动态代理
 * 1.实现的接口
 * 2.目标对象
 * 3.匹配的Advisor
 * 4.BeanFactory    获取真正的Bean Advisor只提供了Bean名
 *
 * @author Dylan
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private static final Log logger = LogFactory.getLog(JdkDynamicAopProxy.class);

    private String beanName;

    private Object target;

    private List<Advisor> matchAdvisors;

    private BeanFactory beanFactory;

    public JdkDynamicAopProxy(String beanName, Object target, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
        this.beanName = beanName;
        this.target = target;
        this.matchAdvisors = matchAdvisors;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getProxy() {

        return this.getClass().getClassLoader();
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {

        if (logger.isDebugEnabled()) {
            logger.debug("为 " + target + "创建Jdk动态代理");
        }
        return Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return AopProxyUtils.applyAdvices(target, method, args, matchAdvisors, proxy, beanFactory);
    }
}
