package com.watent.framework;

import com.watent.framework.advisor.Advisor;
import com.watent.framework.advisor.PointcutAdvisor;
import com.watent.framework.bean.BeanFactory;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用advice 提取出来
 *
 * @author Dylan
 */
public class AopProxyUtils {

    public static Object applyAdvices(Object target, Method method, Object[] args, List<Advisor> matchAdvisors,
                                      Object proxy, BeanFactory beanFactory) throws Throwable {

        // 1、获取要对当前方法进行增强的advice
        List<Object> shouldApplyAdvices = getShouldApplyAdvices(target.getClass(), method, matchAdvisors, beanFactory);
        if (CollectionUtils.isEmpty(shouldApplyAdvices)) {
            return method.invoke(target, args);
        }
        // 责任链式执行增强
        AopAdviceChainInvocation chain = new AopAdviceChainInvocation(proxy, target, method, args, shouldApplyAdvices);
        return chain.invoke();
    }

    /**
     * 获取与方法匹配的切面
     *
     * @param beanClass     Class对象
     * @param method        调用的方法
     * @param matchAdvisors 匹配此类的切面
     * @param beanFactory   Bean工厂
     * @return 切面对象
     * @throws Throwable e
     */
    public static List<Object> getShouldApplyAdvices(Class<?> beanClass, Method method, List<Advisor> matchAdvisors,
                                                     BeanFactory beanFactory) throws Throwable {

        if (org.springframework.util.CollectionUtils.isEmpty(matchAdvisors)) {
            return null;
        }
        List<Object> advices = new ArrayList<>();
        for (Advisor advisor : matchAdvisors) {
            if (advisor instanceof PointcutAdvisor) {
                if (((PointcutAdvisor) advisor).getPointcut().matchMethod(method, beanClass)) {
                    advices.add(beanFactory.getBean(advisor.getAdviceBeanName()));
                }
            }
        }
        return advices;
    }

}
