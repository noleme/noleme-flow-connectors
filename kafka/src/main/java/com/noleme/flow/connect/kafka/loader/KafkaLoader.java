package com.noleme.flow.connect.kafka.loader;

import com.noleme.flow.actor.loader.Loader;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class KafkaLoader<K, V> implements Loader<V>
{
    private static final Logger logger = LoggerFactory.getLogger(KafkaLoader.class);

    private final String topic;
    private final Producer<K, V> producer;
    private final Function<V, K> keyMapper;
    private final boolean autoflush;

    public KafkaLoader(String topic, Producer<K, V> producer, Function<V, K> keyMapper)
    {
        this(topic, producer, keyMapper, true);
    }

    public KafkaLoader(String topic, Producer<K, V> producer, Function<V, K> keyMapper, boolean autoflush)
    {
        this.topic = topic;
        this.producer = producer;
        this.keyMapper = keyMapper;
        this.autoflush = autoflush;
    }

    @Override
    public void load(V input)
    {
        logger.info("Loading message into kafka topic: {} ({})", this.topic, (this.autoflush ? "autoflush" : "no-flush"));

        K key = this.keyMapper.apply(input);

        ProducerRecord<K, V> record = new ProducerRecord<>(this.topic, key, input);
        this.producer.send(record);

        if (this.autoflush)
            this.producer.flush();
    }
}
