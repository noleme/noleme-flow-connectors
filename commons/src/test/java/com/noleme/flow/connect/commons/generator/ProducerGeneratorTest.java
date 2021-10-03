package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public class ProducerGeneratorTest
{
    @Test
    void testIterativeGenerator() throws CompilationException, RunException
    {
        int max = 100_000;
        var flow = Flow
            .stream(() -> new StatefulProducerGenerator<>(
                v -> v + 1,
                v -> v < max,
                0
            ))
            .accumulate(Collection::size)
            .collect()
        ;

        Assertions.assertEquals(max, Flow.runAsPipeline(flow).get(flow));
    }
}
