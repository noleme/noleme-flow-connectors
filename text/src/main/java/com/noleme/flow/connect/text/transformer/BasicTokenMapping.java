package com.noleme.flow.connect.text.transformer;

import com.noleme.flow.actor.transformer.Transformer;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/17
 */
public class BasicTokenMapping <CI extends Collection<String>, CO extends Collection<String>> implements Transformer<CI, CO>
{
    private final Function<String, String> mapper;
    private final Collector<String, ?, CO> collector;

    public BasicTokenMapping(Function<String, String> mapper, Collector<String, ?, CO> collector)
    {
        this.mapper = mapper;
        this.collector = collector;
    }

    @Override
    public CO transform(CI tokens)
    {
        return tokens.stream()
            .map(this.mapper)
            .collect(this.collector)
        ;
    }
}
