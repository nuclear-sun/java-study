package com.sun.clazz;

import javassist.*;

import java.lang.reflect.Method;

/**
 * https://www.cnblogs.com/rickiyang/p/11336268.html
 */
public class Main {

    public static void main(String[] args) {
        ClassPool classPool = ClassPool.getDefault();

        try {
            CtClass ctClass = classPool.get("com.sun.clazz.Person");

            // add setter for name
            CtField ctField = ctClass.getField("name");
            CtMethod setter = CtNewMethod.setter("setName", ctField);
            ctClass.addMethod(setter);

            Class<?> enhancedClass = ctClass.toClass();
            Method method = enhancedClass.getMethod("setName", String.class);
            IPerson person = (IPerson) enhancedClass.newInstance();
            method.invoke(person, "zinc");
            person.printName();

        } catch (Exception e) {

        }
    }

}

interface IPerson {

    void printName();
}

class Person implements IPerson {
    String name;

    public void printName() {
        System.out.println(this.name);
    }
}
