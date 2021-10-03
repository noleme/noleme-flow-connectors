package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;

import java.util.function.Predicate;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public class ProducerGenerator<T> implements Generator<T>
{
    private final Producer<T> supplier;
    private final Predicate<T> condition;
    private T lastValue;

    /**
     *
     * @param supplier
     * @param condition
     */
    public ProducerGenerator(Producer<T> supplier, Predicate<T> condition)
    {
        this.supplier = supplier;
        this.condition = condition;
    }

    @Override
    public boolean hasNext()
    {
        return this.condition.test(this.lastValue);
    }

    @Override
    public T generate() throws GenerationException
    {
        this.lastValue = this.supplier.produce();
        return this.lastValue;
    }

    public interface Producer<T>
    {
        T produce() throws GenerationException;
    }
}
