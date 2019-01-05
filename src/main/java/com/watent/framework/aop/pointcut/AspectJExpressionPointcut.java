package com.watent.framework.pointcut;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * AspectJ 表达式切点
 *
 * @author Dylan
 */
public class AspectJExpressionPointcut implements Pointcut {

    private static PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

    private String expression;

    private PointcutExpression pe;

    public AspectJExpressionPointcut(String expression) {
        this.expression = expression;
        this.pe = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matchClass(Class<?> targetClass) {
        return pe.couldMatchJoinPointsInType(targetClass);
    }

    @Override
    public boolean matchMethod(Method method, Class<?> targetClass) {
        //方法匹配器
        ShadowMatch sm = pe.matchesMethodExecution(method);
        return sm.alwaysMatches();
    }

    public String getExpression() {
        return expression;
    }
}
