package com.watent.test.v3;

import com.watent.framework.bean.DefaultBeanFactory;
import com.watent.test.v2.BookBean;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;
import org.aspectj.weaver.tools.TypePatternMatcher;

import java.lang.reflect.Method;

public class AspectJTest {
    public static void main(String[] args) throws Exception {
        PointcutParser pp = PointcutParser
                .getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();
        TypePatternMatcher tpm = pp.parseTypePattern("com.watent.test..*");
        PointcutExpression pe = pp.parsePointcutExpression("execution(* com.watent.test.start*(..))");
        Class<?> cl = BookBean.class;
        Method aMethod = cl.getMethod("getName", null);
        ShadowMatch sm = pe.matchesMethodExecution(aMethod);

        System.out.println(sm.alwaysMatches());
        System.out.println(pe.couldMatchJoinPointsInType(cl));
        System.out.println(pe.couldMatchJoinPointsInType(DefaultBeanFactory.class));

        for (Method m : cl.getMethods()) {
            System.out.println(m.getName());
        }
    }
}
