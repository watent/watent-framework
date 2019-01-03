package com.watent.framework;

import com.watent.framework.advisor.Advisor;
import com.watent.framework.bean.BeanDefinition;
import com.watent.framework.bean.BeanFactory;
import com.watent.framework.bean.DefaultBeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 实现 Cglib 动态代理
 * 1.需要继承的类
 * 2.需要实现的接口
 * 3.构造参数类型
 * 4.构造参数
 * 5.目标对象
 * 6.匹配的advisors
 * 7.BeanFactory
 * <p>
 * 需要 3 4
 * 目标类没有无参构造方法 子类创建会调用父类的 所以要参数
 *
 * @author Dylan
 */
public class CglibDynamicAopProxy implements AopProxy, MethodInterceptor {

    private static final Log logger = LogFactory.getLog(CglibDynamicAopProxy.class);

    private static Enhancer enhancer = new Enhancer();

    private String beanName;

    private Object target;

    private List<Advisor> matchAdvisors;

    private BeanFactory beanFactory;

    public CglibDynamicAopProxy(String beanName, Object target, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
        this.beanName = beanName;
        this.target = target;
        this.matchAdvisors = matchAdvisors;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getProxy() {
        return this.getProxy(getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("为 " + target + " 创建cglib代理.");
        }
        Class<?> superClass = this.target.getClass();
        enhancer.setSuperclass(superClass);
        enhancer.setInterfaces(superClass.getInterfaces());
        enhancer.setCallback(this);

        Constructor<?> constructor = null;
        try {
            constructor = target.getClass().getConstructor();
        } catch (NoSuchMethodException e) {
            //do none
        }
        // 判断有无无参构造函数 有参构造函数需要 参数类型&参数值
        if (null != constructor) {
            return enhancer.create();
        } else {
            BeanDefinition bd = ((DefaultBeanFactory) beanFactory).getBeanDefinition(beanName);
            return enhancer.create(bd.getConstructor().getParameterTypes(), bd.getConstructorArgumentRealValues());
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return AopProxyUtils.applyAdvices(target, method, args, matchAdvisors, o, beanFactory);
    }
}
