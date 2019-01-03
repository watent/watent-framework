package com.watent.framework;

/**
 * 代理逻辑抽象
 * <p>
 * JdkDynamicAopProxy -> InvocationHandler
 * CglibDynamicAopProxy -> MethodInterceptor
 * ->AopProxy(抽象出获取代理对象的方法)
 */
public interface AopProxy {

    /**
     * 创建代理对象
     */
    Object getProxy();

    /**
     * 使用类加载器创建代理对象
     */
    Object getProxy(ClassLoader classLoader);
}
