package com.watent.framework.context;

/**
 * xml/annotation 解析
 */
public interface BeanDefinitionReader {

    void loadBeanDefinition(Resource resource);

    void loadBeanDefinition(Resource... resources);

}
