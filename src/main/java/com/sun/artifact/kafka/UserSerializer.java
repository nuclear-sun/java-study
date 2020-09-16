package com.sun.artifact.kafka;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserSerializer implements Serializer<User> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        System.out.println("config: UserSerializer");
    }

    @Override
    public void close() {
        System.out.println("close: UserSerializer");
    }

    @Override
    public byte[] serialize(String s, User user) {

        return SerializationUtils.serialize(user);
    }
}
