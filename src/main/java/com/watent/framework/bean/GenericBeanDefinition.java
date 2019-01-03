package com.watent.framework.bean;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 通用 BeanDefinition 实现
 *
 * @author Dylan
 */
public class GenericBeanDefinition implements BeanDefinition {

    private Class<?> beanClass;

    private String scope = BeanDefinition.SCOPE_SINGLETON;

    private String factoryBeanName;

    private String factoryMethodName;

    private String initMethodName;

    private String destroyMethodName;

    private Constructor<?> constructor;

    private Method factoryMethod;

    private List<?> constructorArgumentValues;

    private List<PropertyValue> propertyValues;

    /**
     * 原型实例共享 保证线程安全性
     */
    private ThreadLocal<Object[]> realConstructorArgumentValues = new ThreadLocal<>();

    @Override
    public List<?> getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    @Override
    public Object[] getConstructorArgumentRealValues() {
        return realConstructorArgumentValues.get();
    }

    @Override
    public void setConstructorArgumentRealValues(Object[] values) {
        realConstructorArgumentValues.set(values);
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public String getInitMethodName() {
        return initMethodName;
    }

    @Override
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Method getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    @Override
    public boolean isSingleton() {
        return scope.equals(BeanDefinition.SCOPE_SINGLETON);
    }

    @Override
    public boolean isPrototype() {
        return scope.equals(BeanDefinition.SCOPE_PROTOTYPE);
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setScope(String scope) {
        if (StringUtils.isNotBlank(scope)) {
            this.scope = scope;
        }
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public void setConstructorArgumentValues(List<?> constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public String toString() {
        return "GenericBeanDefinition{" +
                "beanClass=" + beanClass +
                ", scope='" + scope + '\'' +
                ", factoryBeanName='" + factoryBeanName + '\'' +
                ", factoryMethodName='" + factoryMethodName + '\'' +
                ", initMethodName='" + initMethodName + '\'' +
                ", destroyMethodName='" + destroyMethodName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericBeanDefinition that = (GenericBeanDefinition) o;

        if (beanClass != null ? !beanClass.equals(that.beanClass) : that.beanClass != null) return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;
        if (factoryBeanName != null ? !factoryBeanName.equals(that.factoryBeanName) : that.factoryBeanName != null)
            return false;
        if (factoryMethodName != null ? !factoryMethodName.equals(that.factoryMethodName) : that.factoryMethodName != null)
            return false;
        if (initMethodName != null ? !initMethodName.equals(that.initMethodName) : that.initMethodName != null)
            return false;
        return destroyMethodName != null ? destroyMethodName.equals(that.destroyMethodName) : that.destroyMethodName == null;
    }

    @Override
    public int hashCode() {
        int result = beanClass != null ? beanClass.hashCode() : 0;
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        result = 31 * result + (factoryBeanName != null ? factoryBeanName.hashCode() : 0);
        result = 31 * result + (factoryMethodName != null ? factoryMethodName.hashCode() : 0);
        result = 31 * result + (initMethodName != null ? initMethodName.hashCode() : 0);
        result = 31 * result + (destroyMethodName != null ? destroyMethodName.hashCode() : 0);
        return result;
    }
}
