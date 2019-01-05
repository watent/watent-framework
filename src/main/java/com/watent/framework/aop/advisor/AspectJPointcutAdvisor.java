package com.watent.framework.aop.advisor;

import com.watent.framework.aop.pointcut.AspectJExpressionPointcut;
import com.watent.framework.aop.pointcut.Pointcut;

/**
 * AspectJ 类型切点通知实现
 *
 * @author Dylan
 */
public class AspectJPointcutAdvisor implements PointcutAdvisor {

    private String adviceBeanName;

    private String expression;

    private AspectJExpressionPointcut pointcut;

    public AspectJPointcutAdvisor(String adviceBeanName, String expression) {
        this.adviceBeanName = adviceBeanName;
        this.expression = expression;
        this.pointcut = new AspectJExpressionPointcut(expression);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public String getAdviceBeanName() {
        return adviceBeanName;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
