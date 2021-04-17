package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/17
 */
public class JsonArrayToCollection<T, C extends Collection<T>> implements Transformer<ArrayNode, C>
{
    private final Function<JsonNode, T> mapper;
    private final Collector<T, ?, C> collector;

    /**
     *
     * @param mapper
     * @param collector
     */
    public JsonArrayToCollection(Function<JsonNode, T> mapper, Collector<T, ?, C> collector)
    {
        this.mapper = mapper;
        this.collector = collector;
    }

    @Override
    public C transform(ArrayNode json) throws TransformationException
    {
        var iterator = json.elements();

        return Stream.generate(() -> null)
            .takeWhile(x -> iterator.hasNext())
            .map(n -> iterator.next())
            .map(this.mapper)
            .collect(this.collector)
        ;
    }
}
