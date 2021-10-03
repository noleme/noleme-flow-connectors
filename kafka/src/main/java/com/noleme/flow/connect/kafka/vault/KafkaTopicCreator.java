package com.noleme.flow.connect.kafka.vault;

import com.noleme.flow.connect.kafka.config.KafkaConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class KafkaTopicCreator
{
    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicCreator.class);

    private final KafkaConfig config;

    public KafkaTopicCreator(KafkaConfig config)
    {
        this.config = config;
    }

    public void createTopics()
    {
        if (this.config.topics == null || this.config.topics.isEmpty())
            return;

        List<NewTopic> topics = this.config.topics.stream()
            .map(topicConfig -> new NewTopic(
                topicConfig.name,
                Optional.of(topicConfig.numPartition),
                Optional.of(topicConfig.replicationFactor)
            ))
            .collect(Collectors.toList())
        ;

        var props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.config.bootstrapServers);

        try (final AdminClient adminClient = AdminClient.create(props))
        {
            adminClient.createTopics(topics).all().get();
        }
        catch (final InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (e.getCause() instanceof TopicExistsException)
            {
                logger.debug("Topic already exists: {}", e.getMessage());
                return;
            }
            throw new RuntimeException(e);
        }
    }
}
