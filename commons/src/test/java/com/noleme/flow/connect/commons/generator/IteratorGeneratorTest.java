package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.io.input.Input;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 17/04/2021
 */
public class IteratorGeneratorTest
{
    @Test
    void testItemCount() throws CompilationException, RunException
    {
        var flow = Flow
            .<Iterator<Integer>>from("iterator")
            .stream(IteratorGenerator::new)
            .accumulate()
            .collect()
        ;

        var it5 = List.of(0, 1, 2, 3, 4).iterator();
        var it10 = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();

        Assertions.assertEquals(5, Flow.runAsPipeline(Input.of("iterator", it5), flow).get(flow).size());
        Assertions.assertEquals(10, Flow.runAsPipeline(Input.of("iterator", it10), flow).get(flow).size());
    }

    @Test
    void testOrdering() throws CompilationException, RunException
    {
        var flow = Flow
            .<Iterator<Integer>>from("iterator")
            .stream(IteratorGenerator::new)
            .accumulate()
            .pipe(ArrayList::new)
            .collect()
        ;

        var list5 = List.of(0, 1, 2, 3, 4);
        var list10 = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Assertions.assertEquals(list5, Flow.runAsPipeline(Input.of("iterator", list5.iterator()), flow).get(flow));
        Assertions.assertEquals(list10, Flow.runAsPipeline(Input.of("iterator", list10.iterator()), flow).get(flow));
    }
}
