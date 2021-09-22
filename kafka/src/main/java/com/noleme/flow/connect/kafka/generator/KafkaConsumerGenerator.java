package com.noleme.flow.connect.kafka.generator;

import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public class KafkaConsumerGenerator<K, V> implements Generator<V>
{
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerGenerator.class);

    private final Consumer<K, V> consumer;
    private final Duration pollTimeout;
    private final Collection<String> topics;
    private final Queue<ConsumerRecord<K, V>> queue;
    private boolean isStarted;

    public KafkaConsumerGenerator(Consumer<K, V> consumer, String... topics)
    {
        this(consumer, Duration.ofMillis(500), topics);
    }

    public KafkaConsumerGenerator(Consumer<K, V> consumer, Duration pollTimeout, String... topics)
    {
        this(consumer, pollTimeout, List.of(topics));
    }

    public KafkaConsumerGenerator(Consumer<K, V> consumer, Duration pollTimeout, Collection<String> topics)
    {
        this.topics = topics;
        this.consumer = consumer;
        this.pollTimeout = pollTimeout;
        this.queue = new LinkedList<>();
        this.isStarted = false;

        this.consumer.subscribe(topics);
        /*this.consumer.assign(topics.stream()
            .map(topic -> new TopicPartition(topic, 1))
            .collect(Collectors.toList())
        );*/
    }

    @Override
    public boolean hasNext()
    {
        return !this.isStarted || !this.queue.isEmpty();
    }

    @Override
    public V generate() throws GenerationException
    {
        logger.info("Receiving message from kafka topics: {}", this.topics);

        if (!this.queue.isEmpty())
        {
            logger.info("Polling message from in-memory queue (queue size: {})", this.queue.size());
            ConsumerRecord<K, V> next = this.queue.poll();
            return next.value();
        }

        this.isStarted = true;
        while (this.queue.isEmpty())
        {
            for (ConsumerRecord<K, V> record : this.consumer.poll(this.pollTimeout))
                this.queue.add(record);
        }

        return this.queue.poll().value();
    }
}
