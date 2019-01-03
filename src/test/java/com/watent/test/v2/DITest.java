package com.watent.test.v2;

import com.watent.framework.bean.BeanReference;
import com.watent.framework.bean.GenericBeanDefinition;
import com.watent.framework.bean.PreBuildBeanFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dylan
 */
public class DITest {

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void ConstructorTest() throws Throwable {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBean.class);
        List<Object> args = new ArrayList<>();
        args.add("Java");
        args.add(new BeanReference("userBean"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("bookBean", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(UserBean.class);
        args = new ArrayList<>();
        args.add("userBean1");
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("userBean", bd);

        bf.preInstantiateSingletons();

        BookBean bookBean = (BookBean) bf.getBean("bookBean");
//        bookBean.doSomething();
    }

    @Test
    public void testStaticFactoryMethodDI() throws Throwable {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBeanFactory.class);
        bd.setFactoryMethodName("getInstance");
        List<Object> args = new ArrayList<>();
        args.add("入门");
        args.add(new BeanReference("userBean02"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("bookBean02", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(UserBean.class);
        args = new ArrayList<>();
        args.add("小明");
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("userBean02", bd);

        bf.preInstantiateSingletons();

        BookBean bookBean = (BookBean) bf.getBean("bookBean02");
//        bookBean.doSomething();
    }

    @Test
    public void testFactoryMethodDI() throws Throwable {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setFactoryBeanName("bookBeanFactory");
        bd.setFactoryMethodName("create");
        List<Object> args = new ArrayList<>();
        args.add("三体");
        args.add(new BeanReference("userBean02"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("bookBean03", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBeanFactory.class);
        bf.registerBeanDefinition("bookBeanFactory", bd);

        bf.preInstantiateSingletons();

        BookBean bookBean = (BookBean) bf.getBean("bookBean03");

//        bookBean.doSomething();
    }

    @Test
    public void testChildTypeDI() throws Throwable {

        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBean.class);
        List<Object> args = new ArrayList<>();
        args.add("异类");
        args.add(new BeanReference("studentBean"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("bookBean04", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(StudentBean.class);
        args = new ArrayList<>();
        args.add("小学生");
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("studentBean", bd);
        bf.preInstantiateSingletons();

        BookBean bookBean = (BookBean) bf.getBean("bookBean04");
//        bookBean.doSomething();
    }
}
