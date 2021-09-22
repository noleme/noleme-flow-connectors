package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public class TimedProducerGenerator<T> implements Generator<T>
{
    private final ProducerGenerator.Producer<T> supplier;
    private final Predicate<T> condition;
    private final long maxElapsedTime;
    private T lastValue;
    private Instant start;

    /**
     *
     * @param supplier
     * @param condition
     */
    public TimedProducerGenerator(ProducerGenerator.Producer<T> supplier, Predicate<T> condition, long maxElapsedTime)
    {
        this.supplier = supplier;
        this.condition = condition;
        this.maxElapsedTime = maxElapsedTime;
    }

    @Override
    public boolean hasNext()
    {
        if (this.start != null && Duration.between(this.start, Instant.now()).toMillis() > this.maxElapsedTime)
            return false;

        return this.condition.test(this.lastValue);
    }

    @Override
    public T generate() throws GenerationException
    {
        if (this.start == null)
            this.start = Instant.now();

        this.lastValue = this.supplier.produce();
        return this.lastValue;
    }
}
