package com.noleme.flow.connect.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class TestConsumer
{
    public static void main(final String[] args)
    {
        var props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Add additional properties.
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonDeserializer");

        //props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, DataRecord.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("test"));

        int total_count = 0;

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

                for (ConsumerRecord<String, String> record : records)
                {
                    String key = record.key();
                    String value = record.value();
                    total_count += 1;
                    System.out.printf("Consumed record with key %s and value %s, and updated total count to %d%n", key, value, total_count);
                }
            }
        }
        finally {
            consumer.close();
        }
    }
}
