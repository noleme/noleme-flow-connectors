package com.noleme.flow.connect.commons.accumulator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/08/2021
 */
public final class Accumulators
{
    private Accumulators() {}

    public static <T, C extends Collection<T>> List<T> concat(Collection<C> collections)
    {
        List<T> concatenated = new ArrayList<>();
        collections.forEach(concatenated::addAll);
        return concatenated;
    }
}
