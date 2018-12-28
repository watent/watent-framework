package com.watent.test;

/**
 * @author Dylan
 */
public class TestBeanFactory {

    public static TestBean getInstance() {
        return new TestBean();
    }

    public TestBean create() {
        return new TestBean();
    }
}
