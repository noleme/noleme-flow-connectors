package com.noleme.flow.connect.kafka;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.generator.IterableGenerator;
import com.noleme.flow.connect.kafka.generator.KafkaConsumerGenerator;
import com.noleme.flow.connect.kafka.loader.KafkaLoader;
import com.noleme.flow.connect.kafka.vault.KafkaModule;
import com.noleme.vault.Vault;
import com.noleme.vault.exception.VaultException;
import com.noleme.vault.factory.VaultFactory;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

import java.util.List;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
@SuppressWarnings("unchecked")
public class FlowProducer
{
    public static void main(String[] args) throws VaultException, CompilationException, RunException
    {
        VaultFactory.defaultParser.register(new KafkaModule());

        try (var vault = Vault.with("vault/noleme-flow-connect-kafka/kafka.yml"))
        {
            Producer<String, String> producer = (Producer<String, String>)vault.instance(Producer.class, "kafka_default_producer");
            Consumer<String, String> consumer = (Consumer<String, String>)vault.instance(Consumer.class, "kafka_default_consumer");

            var setup = Flow
                .from(() -> List.of("abc", "def", "ghi", "klm"))
                .stream(IterableGenerator::new)
                .sink(new KafkaLoader<>(producer, v -> "my_key", "test_in"))
            ;

            //producer.flush();

            var flow = Flow
                .stream(() -> new KafkaConsumerGenerator<>(consumer, "test_in"))
                .pipe(msg -> "received:" + msg)
                .driftSink(System.out::println)
                .sink(new KafkaLoader<>(producer, v -> "my_key", "test_out"))
            ;

            Flow.runAsParallel(8, setup, flow);
        }
    }
}
