package com.sun.network.rpc;

import java.io.*;

public class SerDeUtil {

    private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);


    public static byte[] serialize(Serializable object) throws IOException {
        byteArrayOutputStream.reset();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }
}
