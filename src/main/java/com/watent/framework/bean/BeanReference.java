package com.watent.framework.bean;

/**
 * 引用Bean 标识对象类型
 *
 * @author Dylan
 */
public class ReferenceBean {

    private String beanName;

    public ReferenceBean(String beanName) {
        super();
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
