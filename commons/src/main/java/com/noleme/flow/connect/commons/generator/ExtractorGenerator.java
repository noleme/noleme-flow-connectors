package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.extractor.ExtractionException;
import com.noleme.flow.actor.extractor.Extractor;
import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;

import java.util.function.Function;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/19
 */
public class ExtractorGenerator <I, O> implements Generator<O>
{
    private final IterableGenerator<I> engine;
    private final Function<I, Extractor<O>> extractorFunction;

    /**
     *
     * @param iterable
     * @param extractorFunction
     */
    public ExtractorGenerator(Iterable<I> iterable, Function<I, Extractor<O>> extractorFunction)
    {
        this.engine = new IterableGenerator<>(iterable);
        this.extractorFunction = extractorFunction;
    }

    @Override
    public boolean hasNext()
    {
        return this.engine.hasNext();
    }

    @Override
    public O generate() throws GenerationException
    {
        try {
            I next = this.engine.generate();
            return this.extractorFunction.apply(next).extract();
        }
        catch (ExtractionException e) {
            throw new GenerationException(e.getMessage(), e);
        }
    }
}
