package com.sun.loader;

public class Main {

    public static void main(String[] args) {

        System.out.println("Main's class loader: " + Main.class.getClassLoader().getClass().getName());

        new A().fun();

    }
}

class A {

    public void fun() {
        System.out.println("A's class loader: " + getClass().getClassLoader().getClass().getName());

        new B().fun();
    }
}

class B {

    public void fun() {
        System.out.println("B's class loader: " + getClass().getClassLoader().getClass().getName());
    }
}
