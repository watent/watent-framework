package com.watent.framework.context;

import com.watent.framework.bean.BeanDefinitionRegistry;

import java.io.IOException;

/**
 * @author Dylan
 */
public class AnnotationApplicationContext extends AbstractApplicationContext {

    private ClassPathBeanDefinitionScanner scanner;

    public AnnotationApplicationContext(String... basePackages) throws Throwable {
        this.scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) beanFactory);
        scanner.scan(basePackages);
    }

    @Override
    public Resource getResource(String location) throws IOException {

        return null;
    }
}
