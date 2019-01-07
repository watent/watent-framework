package com.watent.framework.context;

import com.watent.framework.bean.BeanDefinitionRegistry;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dylan
 */
public class XmlApplicationContext extends AbstractApplicationContext {

    private List<Resource> resources;

    private BeanDefinitionReader reader;

    public XmlApplicationContext(String... location) throws Throwable {
        load(location);
        this.reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
        reader.loadBeanDefinition(resources.toArray(new Resource[this.resources.size()]));
    }

    @Override
    public Resource getResource(String location) throws IOException {

        if (StringUtils.isBlank(location)) {
            return null;
        }
        if (location.startsWith(Resource.CLASS_PATH_PREFIX)) {
            return new ClassPathResource(location.substring(Resource.CLASS_PATH_PREFIX.length()));
        } else if (location.startsWith(Resource.File_SYSTEM_PREFIX)) {
            return new FileSystemResource(location.substring(Resource.File_SYSTEM_PREFIX.length()));
        } else {
            return new UrlResource(location);
        }
    }

    private void load(String... locations) throws IOException {
        if (null == resources) {
            resources = new ArrayList<>();
        }
        for (String location : locations) {
            Resource resource = this.getResource(location);
            if (null != resource) {
                resources.add(resource);
            }
        }
    }
}
