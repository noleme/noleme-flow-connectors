package com.noleme.flow.connect.kafka.flow;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.generator.ProducerGenerator;
import com.noleme.flow.connect.kafka.generator.KafkaConsumerGenerator;
import com.noleme.flow.connect.kafka.loader.KafkaLoader;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public class KafkaFlow
{
    private final Producer<String, String> producer;
    private final Consumer<String, String> consumer;

    @Inject
    public KafkaFlow(
        @Named("kafka_default_producer") Producer<String, String> producer,
        @Named("kafka_default_consumer") Consumer<String, String> consumer
    )
    {
        this.producer = producer;
        this.consumer = consumer;
    }

    public void run() throws CompilationException, RunException
    {
        Instant end = Instant.now().plus(2, ChronoUnit.MINUTES);

        var produce = Flow
            .stream(() -> new ProducerGenerator<>(Instant::now, instant -> instant == null || instant.isBefore(end))).setMaxParallelism(2)
            .pipe(Instant::toString)
            .sink(new KafkaLoader<>(this.producer, v -> "my_key", "test_in"))
        ;

        var consume = Flow
            .stream(() -> new KafkaConsumerGenerator<>(this.consumer, "test_in")).setMaxParallelism(6)
            .pipe(msg -> "processed:" + msg)
            .sink(new KafkaLoader<>(this.producer, v -> "my_key", "test_out"))
        ;

        Flow.runAsParallel(8, produce, consume);
    }
}
