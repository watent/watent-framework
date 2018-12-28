package com.watent.test.v1;

/**
 * @author Dylan
 */
public class TestBean {

    public void doSomething() {
        System.out.println(System.currentTimeMillis() + " " + this);
    }

    public void init() {
        System.out.println("TestBean.init() 执行了");
    }

    public void destroy() {
        System.out.println("TestBean.destroy() 执行了");
    }
}
