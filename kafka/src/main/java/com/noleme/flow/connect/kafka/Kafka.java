package com.noleme.flow.connect.kafka;

import com.noleme.flow.connect.kafka.config.KafkaConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public final class Kafka
{
    private Kafka() {}

    public static void createTopic(String topic, KafkaConfig config)
    {
        final NewTopic newTopic = new NewTopic(
            topic,
            Optional.empty(),
            Optional.empty()
        );

        var props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers);

        try (final AdminClient adminClient = AdminClient.create(props)) {
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        }
        catch (final InterruptedException | ExecutionException e) {
            // Ignore if TopicExistsException, which may be valid if topic exists
            if (e.getCause() instanceof TopicExistsException)
                return;
            throw new RuntimeException(e);
        }
    }
}
