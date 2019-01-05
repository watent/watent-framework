package com.watent.framework.aop;

import com.watent.framework.aop.advisor.Advisor;
import com.watent.framework.aop.advisor.AdvisorRegistry;
import com.watent.framework.aop.advisor.PointcutAdvisor;
import com.watent.framework.bean.BeanFactory;
import com.watent.framework.bean.BeanPostProcessor;
import com.watent.framework.aop.pointcut.Pointcut;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * AOP增强处理的观察者实现
 * getMethods() 获取所有的public方法 包括接口父类
 * getDeclaredMethods() 所有方法(含私有) 不包括继承
 *
 * <p>
 * 遍历 Advisor 匹配类&方法
 *
 * @author Dylan
 */
public class AdvisorAutoProxyCreator implements BeanPostProcessor, AdvisorRegistry, BeanFactoryAware {

    private List<Advisor> advisors;

    private BeanFactory beanFactory;

    public AdvisorAutoProxyCreator() {
        this.advisors = new ArrayList<>();
    }

    @Override
    public void registerAdvisor(Advisor advisor) {
        advisors.add(advisor);
    }

    @Override
    public List<Advisor> getAdvisors() {
        return advisors;
    }

    @Override
    public void setBeanFactory(BeanFactory bf) {
        this.beanFactory = bf;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Throwable {

        // 在此判断bean是否需要进行切面增强
        List<Advisor> matchAdvisors = getMatchedAdvisors(bean, beanName);

        //代理增强
        if (CollectionUtils.isNotEmpty(matchAdvisors)) {
            bean = this.createProxy(bean, beanName, matchAdvisors);
        }
        return bean;
    }

    private List<Advisor> getMatchedAdvisors(Object bean, String beanName) {

        if (CollectionUtils.isEmpty(advisors)) {
            return null;
        }
        //获取类所有方法
        Class<?> beanClass = bean.getClass();
        List<Method> allMethods = getAllMethodForClass(beanClass);

        //遍历advisors集合匹配
        List<Advisor> matchAdvisors = new ArrayList<>();
        for (Advisor ad : advisors) {
            if (ad instanceof PointcutAdvisor) {
                if (isPointcutMatchBean((PointcutAdvisor) ad, beanClass, allMethods)) {
                    matchAdvisors.add(ad);
                }
            }
        }
        return matchAdvisors;
    }

    private List<Method> getAllMethodForClass(Class<?> beanClass) {

        List<Method> allMethods = new LinkedList<>();
//        org.springframework.util.ClassUtils.getAllInterfacesForClassAsSet(beanClass);
        Set<Class> classes = new LinkedHashSet<>(ClassUtils.getAllInterfaces(beanClass));
        classes.add(beanClass);
        for (Class c : classes) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(c);
            allMethods.addAll(Arrays.asList(methods));
        }
        return allMethods;
    }

    private boolean isPointcutMatchBean(PointcutAdvisor pa, Class<?> beanClass, List<Method> methods) {

        Pointcut p = pa.getPointcut();
        //判断类匹配 不通包同类名可能无此方法 所以最重以方法匹配为准
        if (!p.matchClass(beanClass)) {
            return false;
        }
        //判断方法匹配
        for (Method m : methods) {
            if (p.matchMethod(m, beanClass)) {
                return true;
            }
        }
        return false;
    }

    private Object createProxy(Object bean, String beanName, List<Advisor> matchAdvisors) throws Throwable {
        // 通过AopProxyFactory工厂去完成选择、和创建代理对象的工作
        return AopProxyFactory.getDefaultAopProxyFactory().createAopProxy(bean, beanName, matchAdvisors, beanFactory)
                .getProxy();
    }

}
