package com.noleme.flow.connect.commons.transformer;

import java.util.Collection;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/08/2021
 */
public final class Transformers
{
    private Transformers() {}

    public static <T> Map<T, Integer> countPerKey(Collection<T> items)
    {
        return combinePerKey(items, item -> 1, (v1, v2) -> v1 + 1);
    }

    public static <T, U> Map<T, U> combinePerKey(Collection<T> items, Function<? super T, ? extends U> mapper, BinaryOperator<U> resolver)
    {
        return items.stream().collect(Collectors.toMap(item -> item, mapper, resolver));
    }
}
