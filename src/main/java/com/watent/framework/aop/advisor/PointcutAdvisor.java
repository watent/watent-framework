package com.watent.framework.aop.advisor;

import com.watent.framework.aop.pointcut.Pointcut;

/**
 * 切点类型的通知
 */
public interface PointcutAdvisor extends Advisor {

    /**
     * 获取通知
     */
    Pointcut getPointcut();
}
