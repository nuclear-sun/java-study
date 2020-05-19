package com.sun.serialization;

import java.io.*;

public class RawSerializer {

    public static void main(String[] args) throws Exception {

        System.out.println(System.getProperty("user.dir"));

        //testSerializable();

        testSerializableWithNulls();


    }


    static void testSerializable() throws IOException, ClassNotFoundException {
        Person person = new Person("sun", 12);
        FileSerializer.writeObjectToFile(person, "zinc");
        Object object = FileSerializer.readObjectFromFile("zinc");
        System.out.println(object);
    }

    static void testSerializableWithNulls() throws IOException, ClassNotFoundException {
        Stu stu = new Stu();

        // serialization fails if called, success if not called
        stu.set();

        FileSerializer.writeObjectToFile(stu, "zinc");
        Object object = FileSerializer.readObjectFromFile("zinc");
        System.out.println(((Stu)object).name);
    }

}


class Person implements Serializable {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

/**
 * Any class not serializable
 */
class NotSerializable {
}

class Stu implements Serializable {

    // if this is null, the object of Stu can be serialized !
    NotSerializable object = null;

    String name = "sun";

    public void set() {
        this.object = new NotSerializable();
    }

}

