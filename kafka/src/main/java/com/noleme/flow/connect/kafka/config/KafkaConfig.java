package com.noleme.flow.connect.kafka.config;

import java.util.List;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class KafkaConfig
{
    public String bootstrapServers;
    public List<TopicConfig> topics;
    public ProducerConfig producers;
    public ConsumerConfig consumers;

    public static class TopicConfig
    {
        public String name;
    }

    public static class ProducerConfig
    {
        public String acks;
        public String keySerializerClass;
        public String valueSerializerClass;
    }

    public static class ConsumerConfig
    {
        public String groupId;
        public String autoOffsetReset;
        public String keyDeserializerClass;
        public String valueDeserializerClass;
    }
}
