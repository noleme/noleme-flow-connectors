package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.io.input.Input;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 17/04/2021
 */
public class IterableGeneratorTest
{
    @Test
    void testItemCount() throws CompilationException, RunException
    {
        var flow = Flow
            .<List<Integer>>from("list")
            .stream(IterableGenerator::new)
            .accumulate()
            .collect()
        ;

        var list5 = List.of(0, 1, 2, 3, 4);
        var list10 = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assertions.assertEquals(5, Flow.runAsPipeline(Input.of("list", list5), flow).get(flow).size());
        Assertions.assertEquals(10, Flow.runAsPipeline(Input.of("list", list10), flow).get(flow).size());
    }

    @Test
    void testOrdering() throws CompilationException, RunException
    {
        var flow = Flow
            .<List<Integer>>from("list")
            .stream(IterableGenerator::new)
            .accumulate()
            .pipe(ArrayList::new)
            .collect()
        ;

        var list5 = List.of(0, 1, 2, 3, 4);
        var list10 = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assertions.assertEquals(list5, Flow.runAsPipeline(Input.of("list", list5), flow).get(flow));
        Assertions.assertEquals(list10, Flow.runAsPipeline(Input.of("list", list10), flow).get(flow));
    }
}
