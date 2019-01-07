package com.watent.framework.context;

import com.watent.framework.bean.BeanDefinitionRegistry;

/**
 * @author Dylan
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefinition(Resource resource) {
        loadBeanDefinition(new Resource[]{resource});
    }

    @Override
    public void loadBeanDefinition(Resource... resources) {
        if (null == resources || resources.length <= 0) {
            return;
        }
        for (Resource r : resources) {
            parseXml(r);
        }
    }

    private void parseXml(Resource resource) {
        // TODO 解析xml文档，获取bean定义 ，创建bean定义对象，注册到BeanDefinitionRegistry中。
    }

}
