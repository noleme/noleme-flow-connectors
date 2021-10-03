package com.noleme.flow.connect.commons;

import com.noleme.flow.Flow;
import com.noleme.flow.FlowOut;
import com.noleme.flow.actor.extractor.ExtractionException;
import com.noleme.flow.actor.extractor.Extractor;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.commons.generator.TimedProducerGenerator;
import com.noleme.flow.stream.StreamPipe;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public final class Streams
{
    public static final Predicate<?> ALWAYS_TRUE = any -> true;

    private Streams() {}

    public static <I, O> StreamPipe<I, O> timed(FlowOut<I> upstream, Transformer<I, O> actor, Function<I, Long> maxElapsedTime)
    {
        return timed(upstream, actor, any -> true, maxElapsedTime);
    }

    public static <I, O> StreamPipe<I, O> timed(FlowOut<I> upstream, Transformer<I, O> actor, Predicate<I> condition, Function<I, Long> maxElapsedTime)
    {
        return upstream
            .stream(value -> new TimedProducerGenerator<>(
                () -> value,
                condition,
                maxElapsedTime.apply(value)
            ))
            .pipe(actor)
        ;
    }

    public static <O> StreamPipe<O, O> timed(Extractor<O> actor, long maxElapsedTime)
    {
        return timed(actor, any -> true, maxElapsedTime);
    }

    public static <O> StreamPipe<O, O> timed(Extractor<O> actor, Predicate<O> condition, long maxElapsedTime)
    {
        return Flow.stream(() -> new TimedProducerGenerator<>(
            () -> null,
            condition,
            maxElapsedTime
        )).pipe(value -> {
            try {
                return actor.extract();
            }
            catch (ExtractionException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        });
    }
}
