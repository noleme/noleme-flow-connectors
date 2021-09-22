package com.noleme.flow.connect.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.noleme.json.Json;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class TestProducer
{
    public static void createTopic(final String topic, final Properties cloudConfig)
    {
        final NewTopic newTopic = new NewTopic(topic, Optional.empty(), Optional.empty());

        try (final AdminClient adminClient = AdminClient.create(cloudConfig)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        }
        catch (final InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (e.getCause() instanceof TopicExistsException)
                return;
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args)
    {
        final String topic = "test";

        // Create topic if needed

        var props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Add additional properties.
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaJsonSerializer");

        createTopic(topic, props);
        Producer<String, String> producer = new KafkaProducer<>(props);

        // Produce sample data
        final long numMessages = 10L;

        for (long i = 0L; i < numMessages; i++)
        {
            String key = "somekey";
            //DataRecord record = new DataRecord(i);
            String record = "Some message";

            System.out.printf("Producing record: %s\t%s%n", key, record);
            producer.send(new ProducerRecord<>(topic, key, record), (metadata, ex) -> {
                if (ex != null)
                    ex.printStackTrace();
                else
                    System.out.printf("Produced record to topic %s partition [%d] @ offset %d%n", metadata.topic(), metadata.partition(), metadata.offset());
            });
        }

        producer.flush();

        System.out.printf("10 messages were produced to topic %s%n", topic);

        producer.close();
    }

    public static class DataRecord {

        Long count;

        public DataRecord() {
        }

        public DataRecord(Long count) {
            this.count = count;
        }

        public Long getCount() {
            return count;
        }

        @Override
        public String toString() {
            try {
                return Json.mapper().writeValueAsString(this);
            }
            catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

    }
}
