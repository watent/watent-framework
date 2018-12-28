package com.watent.test.v1;

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
