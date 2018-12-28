package com.watent.test.v1;

import com.watent.framework.bean.BeanDefinition;
import com.watent.framework.bean.DefaultBeanFactory;
import com.watent.framework.bean.GenericBeanDefinition;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * @author Dylan
 * @
 */
public class DefaultBeanFactoryTest {

    static DefaultBeanFactory bf = new DefaultBeanFactory();

    @Test
    public void testRegister() throws Exception {

        GenericBeanDefinition bd = new GenericBeanDefinition();

        bd.setBeanClass(TestBean.class);
        bd.setScope(BeanDefinition.SCOPE_SINGLETON);
        // bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);

        bd.setInitMethodName("init");
        bd.setDestroyMethodName("destroy");

        bf.registerBeanDefinition("testBean", bd);
        System.out.println("ok");
    }

    @Test
    public void testRegistStaticFactoryMethod() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(TestBeanFactory.class);
        bd.setFactoryMethodName("getInstance");
        bf.registerBeanDefinition("staticTestBean", bd);
        System.out.println("ok");
    }

    @Test
    public void testRegistFactoryMethod() throws Exception {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(TestBeanFactory.class);
        String fbname = "factory";
        bf.registerBeanDefinition(fbname, bd);

        bd = new GenericBeanDefinition();
        bd.setFactoryBeanName(fbname);
        bd.setFactoryMethodName("create");
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);

        bf.registerBeanDefinition("factoryTestBean", bd);
        System.out.println("ok");
    }

    @AfterClass
    public static void init() throws Exception {

        System.out.println("构造方法方式------------");
        for (int i = 0; i < 3; i++) {
            TestBean ab = (TestBean) bf.getBean("testBean");
            ab.doSomething();
        }

        System.out.println("静态工厂方法方式------------");
        for (int i = 0; i < 3; i++) {
            TestBean ab = (TestBean) bf.getBean("staticTestBean");
            ab.doSomething();
        }

        System.out.println("工厂方法方式------------");
        for (int i = 0; i < 3; i++) {
            TestBean ab = (TestBean) bf.getBean("factoryTestBean");
            ab.doSomething();
        }

        bf.close();

    }
}
