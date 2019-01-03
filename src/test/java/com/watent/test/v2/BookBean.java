package com.watent.test.v2;

/**
 * @author Dylan
 */
public class BookBean {

    private String name;

    private UserBean user;

    public BookBean(String name) {
        this.name = name;
    }

    public BookBean(String name, UserBean user) {
        super();
        this.name = name;
        this.user = user;
        System.out.println("调用了含有 UserBean 参数的构造方法");
    }

    public BookBean(String name, StudentBean student) {
        super();
        this.name = name;
        this.user = student;
        System.out.println("调用了含有 StudentBean 参数的构造方法");
    }

    public BookBean(UserBean user) {
        super();
        this.user = user;
    }

    public void init() {
        System.out.println("BookBean.init() 执行了");
    }

    public void destroy() {
        System.out.println("BookBean.destroy() 执行了");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
