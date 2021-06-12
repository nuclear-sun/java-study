package com.sun.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderTest {

    public static void main(String[] args) {
        System.out.println(ClassLoaderTest.class.getClassLoader().getClass().getName());
        List<Integer> list = new ArrayList<>();
        System.out.println(list.getClass().getClassLoader());

        System.out.println("======================");

        ClassLoader myClassLoader = new MyClassLoader();

        try {
            Class<?> aClass = myClassLoader.loadClass(ClassLoaderTest.class.getName());
            Object o = aClass.newInstance();
            System.out.println(o.getClass().getName());
            System.out.println(o instanceof ClassLoaderTest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
