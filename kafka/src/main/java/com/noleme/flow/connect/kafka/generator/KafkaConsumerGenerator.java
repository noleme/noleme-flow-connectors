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
    private boolean isActive;

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
        this.isActive = true;

        this.consumer.subscribe(topics);
        /*this.consumer.assign(topics.stream()
            .map(topic -> new TopicPartition(topic, 1))
            .collect(Collectors.toList())
        );*/
    }

    @Override
    public boolean hasNext()
    {
        return this.isActive;
    }

    @Override
    public V generate() throws GenerationException
    {
        if (!this.queue.isEmpty())
        {
            logger.debug("Polling message from in-memory queue (queue size: {})", this.queue.size());
            return this.poll();
        }

        while (this.isActive && this.queue.isEmpty())
        {
            for (ConsumerRecord<K, V> record : this.consumer.poll(this.pollTimeout))
                this.queue.add(record);
        }

        return this.poll();
    }

    private V poll()
    {
        logger.info("Receiving message from kafka topics: {}", this.topics);
        var record = this.queue.poll();
        return record != null ? record.value() : null; //This should never be null anyway due to checks in generate()
    }

    synchronized public void deactivate()
    {
        this.isActive = false;
    }
}
