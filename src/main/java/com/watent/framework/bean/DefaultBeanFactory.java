package com.watent.framework.bean;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

    /**
     * 记录正在构建的Bean 解决循环依赖
     * doGetBean 递归调用 都会记录在 buildingBeans
     */
    private ThreadLocal<Set<String>> buildingBeans = new ThreadLocal<>();

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

        // 记录正在创建的Bean
        Set<String> buildingBeansSet = this.buildingBeans.get();
        if (null == buildingBeansSet) {
            buildingBeansSet = new HashSet<>();
            buildingBeans.set(buildingBeansSet);
        }
        // 检测循环依赖
        if (buildingBeansSet.contains(beanName)) {
            throw new Exception(beanName + " 循环依赖！" + buildingBeansSet);
        }
        // 记录正在创建的Bean
        buildingBeansSet.add(beanName);

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

        // 创建好实例后，移除创建中记录
        buildingBeansSet.remove(beanName);

        //属性依赖
        this.setPropertyDIValues(beanDefinition, bean);

        doInit(beanDefinition, bean);

        if (beanDefinition.isSingleton()) {
            beanMap.put(beanName, bean);
        }

        return bean;
    }

    private void setPropertyDIValues(BeanDefinition bd, Object instance) throws Exception {

        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (CollectionUtils.isEmpty(propertyValues)) {
            return;
        }
        for (PropertyValue pv : propertyValues) {
            if (StringUtils.isBlank(pv.getName())) {
                return;
            }
            Field field = instance.getClass().getDeclaredField(pv.getName());
            field.setAccessible(true);
            Object rv = pv.getValue();
            Object v = null;
            if (null == rv) {
                v = null;
            } else if (rv instanceof BeanReference) {
                v = doGetBean(pv.getName());
            } else if (rv instanceof Object[]) {
                System.out.println("TODO 处理对象数组中的bean引用");
            } else if (rv instanceof Collection) {
                System.out.println("TODO 处理集合中的bean引用");
            } else if (rv instanceof Properties) {
                System.out.println("TODO 处理属性中的bean引用");
            } else if (rv instanceof Map) {
                System.out.println("TODO 处理Mapø中的bean引用");
            } else {
                v = rv;
            }
            field.set(instance, v);
        }


    }

    private void doInit(BeanDefinition bd, Object instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (StringUtils.isBlank(bd.getInitMethodName())) {
            return;
        }
        Method method = bd.getBeanClass().getMethod(bd.getInitMethodName(), (Class<?>) null);
        method.invoke(instance);
    }

    private Object createInstanceByConstructor(BeanDefinition bd) throws Exception {

        try {
            Object[] args = getConstructorArgumentValues(bd);
            if (null == args) {
                return bd.getBeanClass().newInstance();
            }
            return determineConstructor(bd, args).newInstance(args);
        } catch (Exception e) {
            logger.error("创建bean的实例异常,beanDefinition：" + bd, e);
            throw e;
        }
    }

    private Object[] getConstructorArgumentValues(BeanDefinition bd) throws Exception {

        return getRealValues(bd.getConstructorArgumentValues());
    }

    private Object[] getRealValues(List<?> args) throws Exception {

        if (CollectionUtils.isEmpty(args)) {
            return null;
        }
        Object[] values = new Object[args.size()];
        int i = 0;
        Object v = null;
        for (Object obj : args) {
            if (null == obj) {
                v = null;
            }
            if (obj instanceof BeanReference) {
                v = doGetBean(((BeanReference) obj).getBeanName());
            } else if (obj instanceof Object[]) {
                System.out.println("TODO 处理对象数组中的bean引用");
            } else if (obj instanceof Collection) {
                System.out.println("TODO 处理集合中的bean引用");
            } else if (obj instanceof Properties) {
                System.out.println("TODO 处理properties中的bean引用");
            } else if (obj instanceof Map) {
                System.out.println("TODO 处理Map中的bean引用");
            } else {
                v = obj;
            }
            values[i++] = v;
        }
        return values;
    }

    private Constructor<?> determineConstructor(BeanDefinition bd, Object[] args) throws Exception {

        Constructor<?> ct;
        //无参构造
        if (null == args) {
            return bd.getBeanClass().getConstructor((Class<?>) null);
        }
        //对于原型bean 从第二次开始获取bean实例时 可直接获得第一次缓存的构造方法
        ct = bd.getConstructor();
        if (null != ct) {
            return ct;
        }
        //根据参数类型获取精确匹配的构造方法
        Class<?>[] paramTypes = new Class[args.length];
        int i = 0;
        for (Object p : args) {
            paramTypes[i++] = p.getClass();
        }
        try {
            ct = bd.getBeanClass().getConstructor(paramTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            //不处理
        }
        // 没有精确参数类型匹配的，则遍历匹配所有的构造方法
        // 判断逻辑：先判断参数数量，再依次比对形参类型与实参类型
        if (null == ct) {
            Constructor<?>[] constructors = bd.getBeanClass().getConstructors();
            //最外层标注
            outer:
            for (Constructor c : constructors) {
                Class<?>[] parameterTypes = c.getParameterTypes();
                if (parameterTypes.length == args.length) {
                    for (int j = 0; j < parameterTypes.length; j++) {
                        //判断能否赋值
                        if (!parameterTypes[j].isAssignableFrom(args[j].getClass())) {
                            //跳到最外层标注
                            continue outer;
                        }
                    }
                    ct = c;
                    break;
                }
            }
        }
        if (null != ct) {
            // 对于原型bean 缓存找到的构造方法 方便下次构造实例对象。
            // 在BeanDefinition中获取设置所用构造方法的方法 同时增加从beanDefinition中获取的逻辑
            if (bd.isPrototype()) {
                bd.setConstructor(ct);
            }
            return ct;
        }
        throw new Exception("不存在对应的构造方法！" + bd);
    }

    private Object createInstanceByStaticFactory(BeanDefinition bd) throws Exception {

        Class<?> type = bd.getBeanClass();
        Object[] realValues = getRealValues(bd.getConstructorArgumentValues());
        Method factoryMethod = determineFactoryMethod(bd, realValues, type);
        return factoryMethod.invoke(type, realValues);
    }


    private Method determineFactoryMethod(BeanDefinition bd, Object[] args, Class<?> type) throws NoSuchMethodException {

        //无参工厂方法
        if (null == type) {
            type = bd.getBeanClass();
        }
        if (null == args) {
            return type.getMethod(bd.getFactoryMethodName(), (Class<?>) null);
        }

        Method factoryMethod;
        // 对于原型bean 从第二次开始获取bean实例时 可直接获得第一次缓存的构造方法。
        factoryMethod = bd.getFactoryMethod();
        if (null != factoryMethod) {
            return factoryMethod;
        }
        //精确获取工厂方法
        int i = 0;
        Class<?>[] parameterTypes = new Class[args.length];
        for (Object o : args) {
            parameterTypes[i++] = o.getClass();
        }
        try {
            factoryMethod = type.getMethod(bd.getFactoryMethodName(), parameterTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            //不处理此异常
        }
        //遍历所有方法
        if (null == factoryMethod) {
            Method[] methods = type.getMethods();
            outer:
            for (Method m : methods) {
                if (!m.getName().equals(bd.getFactoryBeanName())) {
                    continue;
                }
                if (m.getParameterTypes().length == args.length) {
                    Class<?>[] mTypes = m.getParameterTypes();
                    for (int j = 0; j < mTypes.length; j++) {
                        if (!mTypes[j].isAssignableFrom(args[j].getClass())) {
                            continue outer;
                        }
                    }
                    factoryMethod = m;
                    break;
                }
            }
        }
        if (null != factoryMethod) {
            if (bd.isPrototype()) {
                bd.setFactoryMethod(factoryMethod);
            }
            return factoryMethod;
        }
        throw new NoSuchMethodException("No such factoryMethod");
    }

    private Object createInstanceByFactory(BeanDefinition bd) throws Exception {

        Object factoryBean = this.doGetBean(bd.getFactoryBeanName());
        Object[] realArgs = this.getRealValues(bd.getConstructorArgumentValues());
        Method m = this.determineFactoryMethod(bd, realArgs, factoryBean.getClass());

        return m.invoke(factoryBean, realArgs);
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
