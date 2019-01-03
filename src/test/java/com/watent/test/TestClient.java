package com.watent.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dylan
 */
public class TestClient {

    @Test
    public void run() {
        try {
            getAllDeclaredMethods();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void getAllDeclaredMethods() throws IllegalAccessException {

        final List<String> strs = new ArrayList<>(32);

        MethodCallback methodCallback = strs::add;

        MethodCallback methodCallback2 = strs::add;

        doWithMethods(methodCallback);
        System.out.println(strs.size());
    }

    public static void doWithMethods(MethodCallback mc) throws IllegalAccessException {

        for (int i = 0; i < 10; i++) {
            mc.doWith(i + "");
        }
    }

    /**
     * Action to take on each method.
     */
    @FunctionalInterface
    public interface MethodCallback {

        void doWith(String str) throws IllegalArgumentException, IllegalAccessException;
    }

}
