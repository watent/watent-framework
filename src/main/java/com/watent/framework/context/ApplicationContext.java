package com.watent.framework.context;

import com.watent.framework.bean.BeanFactory;

/**
 * 应用上下文
 * 承担 加载 解析 创建BeanDefinition职责
 */
public interface ApplicationContext extends BeanFactory, ResourceLoader {

}
