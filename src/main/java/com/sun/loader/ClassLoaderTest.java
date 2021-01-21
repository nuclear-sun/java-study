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

        ClassLoader myClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";

                try {
                    InputStream inputStream = getClass().getResourceAsStream(fileName);

                    if(inputStream == null) {
                        return super.loadClass(name);
                    }

                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (Exception e) {
                    System.out.println("load class failed: " + e.getMessage());
                    throw new ClassNotFoundException();
                }
            }
        };

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
