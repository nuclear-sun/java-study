package com.sun.artifact;

import java.util.ArrayList;
import java.util.List;

public class ClassLoaderTest {

    public static void main(String[] args) {
        System.out.println(ClassLoaderTest.class.getClassLoader().getClass().getName());
        List<Integer> list = new ArrayList<>();
        System.out.println(list.getClass().getClassLoader());

    }
}
