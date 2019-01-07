package com.watent.framework.context;

import com.watent.framework.bean.BeanDefinitionRegistry;
import com.watent.framework.bean.GenericBeanDefinition;
import com.watent.framework.context.config.annotation.Autowired;
import com.watent.framework.context.config.annotation.Component;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

/**
 * @author Dylan
 */
public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private int classPathAbsLength = AnnotationBeanDefinitionReader.class.getResource("/").toString().length();

    public AnnotationBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinition(Resource resource) {
        loadBeanDefinition(new Resource[]{resource});
    }

    @Override
    public void loadBeanDefinition(Resource... resources) {
        for (Resource r : resources) {
            retrieveAndRegistryBeanDefinition(r);
        }
    }

    private void retrieveAndRegistryBeanDefinition(Resource resource) {
        if (null == resource || null == resource.getFile()) {
            return;
        }
        String className = getClassNameFromFile(resource.getFile());
        try {
            Class<?> clazz = Class.forName(className);
            Component component = clazz.getAnnotation(Component.class);
            if (null == component) {
                return;
            }
            GenericBeanDefinition bd = new GenericBeanDefinition();
            bd.setBeanClass(clazz);
            bd.setScope(component.scope());
            bd.setFactoryBeanName(component.factoryBeanName());
            bd.setFactoryMethodName(component.factoryMethodName());
            bd.setInitMethodName(component.initMethodName());
            bd.setDestroyMethodName(component.destroyMethodName());

            // 获得所有构造方法，在构造方法上找@Autowired注解，如有，将这个构造方法set到bd;
            this.handleConstructor(clazz, bd);

            //处理工厂方法参数
            if (StringUtils.isNotBlank(bd.getFactoryMethodName())) {
                handleFactoryMethodArgs(clazz, bd);
            }
            //处理属性依赖
            handlePropertyDI(clazz, bd);

            String beanName = "".equals(component.value()) ? component.name() : null;
            if (StringUtils.isBlank(beanName)) {
                // TODO 应用名称生成规则生成beanName;
            }

            //注册
            registry.registerBeanDefinition(className, bd);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConstructor(Class<?> clazz, GenericBeanDefinition bd) {

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor c : declaredConstructors) {
            if (null == c.getAnnotation(Autowired.class)) {
                continue;
            }
            bd.setConstructor(c);
            Parameter[] ps = c.getParameters();
            // TODO 遍历获取参数上的注解，及创建参数依赖
            break;
        }
    }

    private void handlePropertyDI(Class<?> clazz, GenericBeanDefinition bd) {
        //TODO
    }

    private void handleFactoryMethodArgs(Class<?> clazz, GenericBeanDefinition bd) {
        //TODO
    }

    private String getClassNameFromFile(File file) {

        String absolutePath = file.getAbsolutePath();
        String className = absolutePath.substring(classPathAbsLength + 1, absolutePath.indexOf("."));
        return StringUtils.replace(className, File.separator, ".");

    }

}
