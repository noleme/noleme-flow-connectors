package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.Flow;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.Streams;
import com.noleme.flow.io.input.Input;
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

    @Test
    void testTimedGenerator() throws CompilationException, RunException
    {
        var flow = Streams
            .timed(
                Flow.<Long>from("max_value"),
                val -> {
                    try {
                        Thread.sleep(50);
                        return 1;
                    }
                    catch (InterruptedException e) {
                        throw new TransformationException(e.getMessage(), e);
                    }
                },
                maxTime -> maxTime
            )
            .accumulate(Collection::size)
            .sink(System.out::println)
        ;

        var input1 = Input.of("max_value", 10_000);
        Flow.runAsParallel(input1, flow);
        Flow.runAsPipeline(input1, flow);

        var input2 = Input.of("max_value", 50_000);
        Flow.runAsParallel(input2, flow);
        Flow.runAsPipeline(input2, flow);
    }
}
