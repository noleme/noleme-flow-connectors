package com.noleme.flow.connect.commons;

import com.noleme.flow.CurrentOut;
import com.noleme.flow.slice.PipeSlice;
import com.noleme.flow.slice.SourceSlice;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 04/04/2022
 */
public final class Slices
{
    private Slices() {}

    public static <O> SourceSlice<O> sliceOf(Supplier<CurrentOut<O>> supplier)
    {
        return new SourceSlice<>() {
            @Override
            public CurrentOut<O> out() {
                return supplier.get();
            }
        };
    }

    public static <I, O> PipeSlice<I, O> sliceOf(Function<CurrentOut<I>, CurrentOut<O>> function)
    {
        return new PipeSlice<>() {
            @Override
            public CurrentOut<O> out(CurrentOut<I> upstream) {
                return function.apply(upstream);
            }
        };
    }
}

