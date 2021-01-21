package com.sun.loader;

import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

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

            // defineClass 调用过程中如果发现其他需要加载的类（如 java.lang.Object) 会对该名称递归调用此类加载器的 loadClass 方法
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            System.out.println("load class failed: " + e.getMessage());
            throw new ClassNotFoundException();
        }
    }
}
