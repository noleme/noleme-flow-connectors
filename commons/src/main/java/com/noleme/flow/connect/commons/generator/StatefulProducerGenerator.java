package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;

import java.util.function.Predicate;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public class StatefulProducerGenerator<T> implements Generator<T>
{
    private final Producer<T> supplier;
    private final Predicate<T> condition;
    private T lastValue;

    public StatefulProducerGenerator(Producer<T> supplier, Predicate<T> condition)
    {
        this(supplier, condition, null);
    }

    /**
     *
     * @param supplier
     * @param condition
     * @param initialState
     */
    public StatefulProducerGenerator(Producer<T> supplier, Predicate<T> condition, T initialState)
    {
        this.supplier = supplier;
        this.condition = condition;
        this.lastValue = initialState;
    }

    @Override
    public boolean hasNext()
    {
        return this.condition.test(this.lastValue);
    }

    @Override
    public T generate() throws GenerationException
    {
        this.lastValue = this.supplier.produce(this.lastValue);
        return this.lastValue;
    }

    public interface Producer<T>
    {
        T produce(T previous) throws GenerationException;
    }
}
