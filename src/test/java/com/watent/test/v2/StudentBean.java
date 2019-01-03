package com.watent.test.v2;

/**
 * @author Dylan
 */
public class StudentBean extends UserBean {

    private BookBean book;

    public StudentBean(String name) {
        super(name);
    }

    public StudentBean(String name, BookBean book) {
        super(name);
        this.book = book;
    }

    public void doReading() {
        System.out.println(System.currentTimeMillis() + " " + super.getName() + " book.name=" + this.book.getName());
    }

    public void speakEnglish() {
        System.out.println("Speak English Hello ！");
    }

}
