package com.watent.test.v3;

import com.watent.framework.aop.AdvisorAutoProxyCreator;
import com.watent.framework.aop.advisor.AspectJPointcutAdvisor;
import com.watent.framework.bean.BeanReference;
import com.watent.framework.bean.GenericBeanDefinition;
import com.watent.framework.bean.PreBuildBeanFactory;
import com.watent.test.v2.BookBean;
import com.watent.test.v2.StudentBean;
import com.watent.test.v3.aop.MyAfterReturningAdvice;
import com.watent.test.v3.aop.MyBeforeAdvice;
import com.watent.test.v3.aop.MyMethodInterceptor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AOPTest {

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void testCirculationDI() throws Throwable {

        GenericBeanDefinition bd = new GenericBeanDefinition();
//        bd.setBeanClass(ABean.class);
        bd.setBeanClass(StudentBean.class);
        List<Object> args = new ArrayList<>();
        args.add("学生A");
        args.add(new BeanReference("bookBean"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("studentBean", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBean.class);
        args = new ArrayList<>();
        args.add("数学");
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("bookBean", bd);

        // 前置增强advice bean注册
        bd = new GenericBeanDefinition();
        bd.setBeanClass(MyBeforeAdvice.class);
        bf.registerBeanDefinition("myBeforeAdvice", bd);

        // 环绕增强advice bean注册
        bd = new GenericBeanDefinition();
        bd.setBeanClass(MyMethodInterceptor.class);
        bf.registerBeanDefinition("myMethodInterceptor", bd);

        // 后置增强advice bean注册
        bd = new GenericBeanDefinition();
        bd.setBeanClass(MyAfterReturningAdvice.class);
        bf.registerBeanDefinition("myAfterReturningAdvice", bd);

        // 往BeanFactory中注册AOP的BeanPostProcessor
        AdvisorAutoProxyCreator aapc = new AdvisorAutoProxyCreator();
        bf.registerBeanPostProcessor(aapc);
        // 向AdvisorAutoProxyCreator注册Advisor
        aapc.registerAdvisor(
                new AspectJPointcutAdvisor("myBeforeAdvice", "execution(* com.watent.test.v2.StudentBean.*(..))"));
        // 向AdvisorAutoProxyCreator注册Advisor
        aapc.registerAdvisor(
                new AspectJPointcutAdvisor("myMethodInterceptor", "execution(* com.watent.test.v2.StudentBean.do*(..))"));
        // 向AdvisorAutoProxyCreator注册Advisor
        aapc.registerAdvisor(new AspectJPointcutAdvisor("myAfterReturningAdvice",
                "execution(* com.watent.test.v2.StudentBean.do*(..))"));

        bf.preInstantiateSingletons();

        StudentBean student = (StudentBean) bf.getBean("studentBean");

        student.doReading();
        System.out.println("--------------------------------");
        student.speakEnglish();
    }
}
