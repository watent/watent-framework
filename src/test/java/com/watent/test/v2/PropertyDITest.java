package com.watent.test.v2;

import com.watent.framework.bean.BeanReference;
import com.watent.framework.bean.GenericBeanDefinition;
import com.watent.framework.bean.PreBuildBeanFactory;
import com.watent.framework.bean.PropertyValue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dylan
 */
public class PropertyDITest {

    static PreBuildBeanFactory bf = new PreBuildBeanFactory();

    @Test
    public void testCirculationDI() throws Throwable {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(BookBean.class);

        List<Object> args = new ArrayList<>();
        args.add(new BeanReference("studentBean"));
        bd.setConstructorArgumentValues(args);

        List<PropertyValue> pvs = new ArrayList<>();
        pvs.add(new PropertyValue("name", "QQ"));
        bd.setPropertyValues(pvs);

        bf.registerBeanDefinition("bookBean", bd);

        bd = new GenericBeanDefinition();
        bd.setBeanClass(StudentBean.class);
        args = new ArrayList<>();
        boolean xm = args.add("xm");
        bd.setConstructorArgumentValues(args);
        bf.registerBeanDefinition("studentBean", bd);

        bf.preInstantiateSingletons();
    }
}
