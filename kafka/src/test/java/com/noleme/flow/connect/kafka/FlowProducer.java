package com.noleme.flow.connect.kafka;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.generator.IterableGenerator;
import com.noleme.flow.connect.kafka.loader.KafkaLoader;
import com.noleme.flow.connect.kafka.vault.KafkaModule;
import com.noleme.vault.Vault;
import com.noleme.vault.exception.VaultException;
import com.noleme.vault.factory.VaultFactory;
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

            var flow = Flow
                .from(() -> List.of("abc", "def", "ghi", "klm"))
                .stream(IterableGenerator::new)
                .sink(new KafkaLoader<>("test", producer, v -> "my_key", false)).name("test_loader")
            ;

            producer.flush();

            Flow.runAsPipeline(flow);
        }
    }
}
