package com.watent.framework.context;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author Dylan
 */
public class ClassPathResource implements Resource {

    private String path;

    private Class<?> clazz;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this(path, clazz, null);
    }

    public ClassPathResource(String path, Class<?> clazz, ClassLoader classLoader) {
        this.path = path;
        this.clazz = clazz;
        this.classLoader = classLoader;
    }

    @Override
    public boolean exists() {

        if (StringUtils.isBlank(path)) {
            return false;
        }
        if (null != classLoader) {
            return null != classLoader.getResource(path.startsWith("/") ? path.substring(1) : path);
        }
        if (null != clazz) {
            return null != clazz.getResource(path);
        }
        return false;
    }

    @Override
    public boolean isReadable() {
        return exists();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getInputStream() {

        if (null == path) {
            return null;
        }
        if (null != clazz) {
            return clazz.getResourceAsStream(path);
        }
        if (null != classLoader) {
            return classLoader.getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);
        }
        return null;
    }
}
