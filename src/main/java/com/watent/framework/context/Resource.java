package com.watent.framework.context;

import java.io.File;

/**
 * 加载不通数据来源 向解析提供一致的使用接口
 *
 * @author Dylan
 */
public interface Resource extends InputStreamSource {

    String CLASS_PATH_PREFIX = "classpath:";

    String File_SYSTEM_PREFIX = "file:";

    boolean exists();

    boolean isReadable();

    boolean isOpen();

    File getFile();
}
