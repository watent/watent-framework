package com.watent.framework.advisor;

/**
 * 切面/通知者 组合通知&切点
 * <p>
 * 这里 Pointcut 配置成Bean? 为了易用性设计
 * AdviceBeanName + Expression 组合成切面
 *
 */
public interface Advisor {

    /**
     * 获取通知Bean名
     */
    String getAdviceBeanName();

    /**
     * 获取表达式
     */
    String getExpression();

}
