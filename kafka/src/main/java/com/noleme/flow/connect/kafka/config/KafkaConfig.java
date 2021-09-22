package com.noleme.flow.connect.kafka.config;

import java.util.List;
import java.util.Properties;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class KafkaConfig
{
    public String bootstrapServers;
    public boolean provideDefaultProducer = true;
    public boolean provideDefaultConsumer = true;
    public boolean createTopics = false;
    public List<TopicConfig> topics;
    public Properties producers;
    public Properties consumers;

    public static class TopicConfig
    {
        public String name;
        public int numPartition = 1;
        public short replicationFactor = 1;
    }
}
