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
public class CirculationDITest {

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void testCirculationDI() throws Throwable {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBean.class);
        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("studentBean"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("bookBean", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(StudentBean.class);
        args = new ArrayList<>();
        args.add(new BeanReference("bookBean"));
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("studentBean", bd);

        bf.preInstantiateSingletons();
    }
}
