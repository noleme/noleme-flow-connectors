package com.noleme.flow.connect.kafka;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.generator.IterableGenerator;
import com.noleme.flow.connect.kafka.config.KafkaConfig;
import com.noleme.flow.connect.kafka.loader.KafkaLoader;
import com.noleme.flow.connect.kafka.vault.KafkaModule;
import com.noleme.flow.connect.kafka.vault.KafkaTopicCreator;
import com.noleme.vault.Vault;
import com.noleme.vault.exception.VaultException;
import com.noleme.vault.factory.VaultFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.List;
import java.util.Properties;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class FlowProducer
{
    public static void main(String[] args) throws VaultException, CompilationException, RunException
    {
        VaultFactory.defaultParser.register(new KafkaModule());

        var vault = Vault.with("vault/noleme-flow-connect-kafka/kafka.yml");

        vault.instance(KafkaTopicCreator.class).createTopics();
        KafkaConfig cfg = vault.instance(KafkaConfig.class);

        var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cfg.bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, cfg.producers.acks);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, cfg.producers.keySerializerClass);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, cfg.producers.valueSerializerClass);

        Producer<String, String> producer = new KafkaProducer<>(props);

        var flow = Flow
            .from(() -> List.of("abc", "def", "ghi", "klm"))
            .stream(IterableGenerator::new)
            .sink(new KafkaLoader<>("test", producer, v -> "whatever"))
        ;

        Flow.runAsPipeline(flow);

        producer.close();
    }
}
