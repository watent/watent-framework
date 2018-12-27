package com.watent.framework.bean;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean工厂 默认实现
 *
 * @author Dylan
 */
public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry, Closeable {


    private Logger logger = LoggerFactory.getLogger(DefaultBeanFactory.class);

    private Map<String, Object> beanMap = new ConcurrentHashMap<>(256);

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public Object getBean(String beanName) throws Exception {
        return doGetBean(beanName);
    }

    protected Object doGetBean(String beanName) throws Exception {

        Objects.requireNonNull(beanName, "beanName不能为空");
        Object bean = beanMap.get(beanName);
        if (null != bean) {
            return bean;
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        Objects.requireNonNull(beanDefinition, "beanDefinition不能为空");

        Class<?> beanClass = beanDefinition.getBeanClass();
        if (null != beanClass) {
            if (StringUtils.isBlank(beanDefinition.getFactoryMethodName())) {
                //构造器
                bean = createInstanceByConstructor(beanDefinition);
            } else {
                //静态工厂
                bean = createInstanceByStaticFactory(beanDefinition);
            }
        } else {
            //工厂方法
            bean = createInstanceByFactory(beanDefinition);
        }

        doInit(beanDefinition, bean);

        if (beanDefinition.isSingleton()) {
            beanMap.put(beanName, bean);
        }

        return bean;
    }

    private void doInit(BeanDefinition bd, Object instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (StringUtils.isBlank(bd.getInitMethodName())) {
            return;
        }
        Method method = bd.getBeanClass().getMethod(bd.getInitMethodName(), (Class<?>) null);
        method.invoke(instance);
    }

    private Object createInstanceByConstructor(BeanDefinition bd) throws IllegalAccessException, InstantiationException {

        try {
            return bd.getBeanClass().newInstance();
        } catch (SecurityException e) {
            logger.error("创建bean的实例异常,beanDefinition：" + bd, e);
            throw e;
        }
    }

    private Object createInstanceByStaticFactory(BeanDefinition bd) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = bd.getBeanClass().getMethod(bd.getFactoryMethodName(), (Class<?>) null);
        return method.invoke(null);
    }

    private Object createInstanceByFactory(BeanDefinition bd) throws Exception {

        Object factoryBean = this.doGetBean(bd.getFactoryBeanName());
        Method method = factoryBean.getClass().getMethod(bd.getFactoryMethodName());
        return method.invoke(factoryBean);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {

        Objects.requireNonNull(beanName, "注册bean需要给入beanName");
        Objects.requireNonNull(beanDefinition, "注册bean需要给入beanDefinition");

        // 校验给入的bean是否合法
        if (!beanDefinition.validate()) {
            throw new BeanDefinitionRegistryException("名字为[" + beanName + "] 的bean定义不合法：" + beanDefinition);
        }

        if (this.containsBeanDefinition(beanName)) {
            throw new BeanDefinitionRegistryException(
                    "名字为[" + beanName + "] 的bean定义已存在:" + this.getBeanDefinition(beanName));
        }

        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public Boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public void close() throws IOException {

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {

            String beanName = entry.getKey();
            BeanDefinition bd = entry.getValue();

            if (bd.isSingleton() && StringUtils.isNotBlank(bd.getDestroyMethodName())) {
                Object instance = this.beanMap.get(beanName);
                try {
                    Method destroyMethod = instance.getClass().getMethod(bd.getDestroyMethodName(), (Class<?>) null);
                    destroyMethod.invoke(instance);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    logger.error("执行bean[" + beanName + "] " + bd + " 的 销毁方法异常！", e1);
                }
            }
        }
    }
}
