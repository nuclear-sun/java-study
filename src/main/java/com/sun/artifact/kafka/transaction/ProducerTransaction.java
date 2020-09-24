package com.sun.artifact.kafka.transaction;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;

public class ProducerTransaction {

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos8:9092,centos8_1:9092,centos8_2:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "1");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        producer.initTransactions();


        try {
            producer.beginTransaction();
            for (int i = 0; i < 10; i++) {

                if(i == 7) {
                    throw new Exception("7");
                }

                ProducerRecord<String, String> record = new ProducerRecord<>("topic01", "value" + i);

                producer.send(record);
                producer.flush();

            }
            producer.commitTransaction();
        } catch (Exception e) {
            producer.abortTransaction();
        } finally {
            producer.close();
        }


    }
}
