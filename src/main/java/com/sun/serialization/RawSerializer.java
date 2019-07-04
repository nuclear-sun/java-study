package com.sun.serialization;

import java.io.*;

public class RawSerializer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println(System.getProperty("user.dir"));


//        Person person = new Person("sun", 12);
//        FileSerializer.writeObjectToFile(person, "zinc");
        Object object = FileSerializer.readObjectFromFile("zinc");
        System.out.println(object);

    }

}


class FileSerializer {

    public static void writeObjectToFile(Object object, String file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
    }

    public static Object readObjectFromFile(String file) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
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



