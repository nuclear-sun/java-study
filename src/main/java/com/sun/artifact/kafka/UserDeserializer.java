package com.sun.artifact.kafka;


import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class UserDeserializer implements Deserializer<User> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        System.out.println("config: UserDeserializer");
    }

    @Override
    public void close() {

        System.out.println("close UserDeserializer");
    }

    @Override
    public User deserialize(String topic, byte[] data) {

        return SerializationUtils.deserialize(data);
    }
}
