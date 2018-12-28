package com.watent.test.v2;

/**
 * @author Dylan
 */
public class StudentBean extends UserBean {

    private BookBean book;

    public StudentBean(String name) {
        super(name);
    }

    public StudentBean(BookBean book) {
        super("none");
        this.book = book;
    }
}
