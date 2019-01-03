package com.watent.framework.advisor;

import com.watent.framework.pointcut.Pointcut;

/**
 * 切点类型的通知
 */
public interface PointcutAdvisor extends Advisor {

    /**
     * 获取通知
     */
    Pointcut getPointcut();
}
