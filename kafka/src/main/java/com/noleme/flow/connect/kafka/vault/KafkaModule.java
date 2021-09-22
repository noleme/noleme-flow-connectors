package com.noleme.flow.connect.kafka.vault;

import com.noleme.flow.connect.kafka.config.KafkaConfig;
import com.noleme.vault.container.Invocation;
import com.noleme.vault.container.definition.ServiceInstantiation;
import com.noleme.vault.container.definition.ServiceValue;
import com.noleme.vault.container.register.Definitions;
import com.noleme.vault.parser.module.GenericModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class KafkaModule extends GenericModule<KafkaConfig>
{
    private static final String CONFIG_ID = "kafka_config";
    private static final String TOPIC_CREATOR_ID = "kafka_topic_creator";
    private static final String DEFAULT_PRODUCER_ID = "kafka_default_producer";
    private static final String DEFAULT_CONSUMER_ID = "kafka_default_consumer";

    public KafkaModule()
    {
        super("kafka", KafkaConfig.class, KafkaModule::processConfig);
    }

    private static void processConfig(KafkaConfig cfg, Definitions defs)
    {
        if (cfg.topics != null)
            addTopicCreator(cfg, defs);

        if (cfg.producers != null && !cfg.producers.contains(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG))
            cfg.producers.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cfg.bootstrapServers);
        if (cfg.producers != null && cfg.provideDefaultProducer)
            addDefaultProducer(cfg, defs);

        if (cfg.consumers != null && !cfg.consumers.contains(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG))
            cfg.consumers.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cfg.bootstrapServers);
        if (cfg.consumers != null && cfg.provideDefaultConsumer)
            addDefaultConsumer(cfg, defs);

        defs.services().set(CONFIG_ID, new ServiceValue<>(CONFIG_ID, cfg));
    }

    private static void addTopicCreator(KafkaConfig cfg, Definitions defs)
    {
        final Object[] args = { defs.services().reference(CONFIG_ID) };

        var instantiation = new ServiceInstantiation(
            TOPIC_CREATOR_ID,
            KafkaTopicCreator.class.getName(),
            args
        );

        if (cfg.createTopics)
            instantiation.addInvocation(new Invocation("createTopics"));

        defs.services().set(instantiation.getIdentifier(), instantiation);
    }

    private static void addDefaultProducer(KafkaConfig cfg, Definitions defs)
    {
        final Object[] args = { cfg.producers };

        defs.services().set(
            DEFAULT_PRODUCER_ID,
            new ServiceInstantiation(
                DEFAULT_PRODUCER_ID,
                KafkaProducer.class.getName(),
                args
            ).setCloseable(true)
        );
    }

    private static void addDefaultConsumer(KafkaConfig cfg, Definitions defs)
    {
        final Object[] args = { cfg.consumers };

        defs.services().set(
            DEFAULT_CONSUMER_ID,
            new ServiceInstantiation(
                DEFAULT_CONSUMER_ID,
                KafkaConsumer.class.getName(),
                args
            ).setCloseable(true)
        );
    }
}
