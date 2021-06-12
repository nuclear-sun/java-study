package com.sun.loader;

import java.lang.reflect.Method;

public class ClassLoaderContext {

    public static void main(String[] args) throws Exception {

        ClassLoader loader = new MyClassLoader();

        System.out.println("init class loader: " + ClassLoaderContext.class.getClassLoader().getClass().getName());

        Class<?> aClass = loader.loadClass("com.sun.loader.Main");
        Method main = aClass.getDeclaredMethod("main", new Class[]{String[].class});
        System.out.println(main == null);
        main.invoke(null, (Object)new String[]{"a", "b"});
    }

}



