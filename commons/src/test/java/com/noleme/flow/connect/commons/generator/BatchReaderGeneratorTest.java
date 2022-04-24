package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.transformer.filesystem.ResourceStreamer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 24/04/2022
 */
public class BatchReaderGeneratorTest
{
    @Test
    void testItemCount() throws CompilationException, RunException
    {
        var flow = Flow
            .from(new ResourceStreamer(), "csv/test.csv")
            .stream(is -> new BatchReaderGenerator(is, 2))
            .accumulate()
            .collect()
        ;

        Assertions.assertEquals(3, Flow.runAsPipeline(flow).get(flow).size());
    }

    @Test
    void testOrdering() throws CompilationException, RunException
    {
        var flow = Flow
            .from(new ResourceStreamer(), "csv/test.csv")
            .stream(is -> new BatchReaderGenerator(is, 2))
            .accumulate(ls -> ls.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
            )
            .collect()
        ;

        var csv = List.of(
            "first,1",
            "second,2",
            "third,3",
            "fourth,4",
            "fifth,5"
        );

        Assertions.assertLinesMatch(csv, new ArrayList<>(Flow.runAsPipeline(flow).get(flow)));
    }
}
