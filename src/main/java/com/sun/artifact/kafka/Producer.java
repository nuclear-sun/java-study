package com.sun.artifact.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Producer {

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class.getName());
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos8:9092,centos8_1:9092,centos8_2:9092");

        KafkaProducer<String, User> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, User> record = new ProducerRecord<>("topic01", "key" + i, new User("user" + i, i + 10));
            producer.send(record);
        }
        //producer.flush();
        producer.close();

    }
}
