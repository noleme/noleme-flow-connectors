package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.generator.Generator;

import java.util.Iterator;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public class IteratorGenerator<T> implements Generator<T>
{
    private final Iterator<T> iterator;

    /**
     *
     * @param iterator
     */
    public IteratorGenerator(Iterator<T> iterator)
    {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    @Override
    public T generate()
    {
        return this.iterator.hasNext() ? this.iterator.next() : null;
    }
}
