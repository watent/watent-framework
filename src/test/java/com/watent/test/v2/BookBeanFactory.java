package com.watent.test.v2;

public class BookBeanFactory {

    public static BookBean getInstance(String name, UserBean user) {
        return new BookBean(name, user);
    }

    public BookBean create(String name, UserBean cb) {
        return new BookBean(name, cb);
    }
}
