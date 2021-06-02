package com.noleme.flow.connect.wordcount;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.accumulator.Accumulators;
import com.noleme.flow.connect.commons.generator.ReaderGenerator;
import com.noleme.flow.connect.commons.loader.file.FileWriteString;
import com.noleme.flow.connect.commons.transformer.Transformers;
import com.noleme.flow.connect.commons.transformer.filesystem.FileStreamer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 04/08/2021
 */
public class Main
{
    public static void main(String[] args) throws CompilationException, RunException
    {
        var flow = Flow
            .from(new FileStreamer(), "data/test.txt")
            .stream(ReaderGenerator::new)
            .pipe(line -> List.of(line.split("[^\\p{L}]+")))
            .accumulate(Accumulators::concat)
            .pipe(Transformers::countPerKey)
            .pipe(wordCount -> wordCount.entrySet().stream()
                .map(wc -> wc.getKey() + ": "+ wc.getValue())
                .collect(Collectors.joining("\n"))
            )
            .sink(new FileWriteString("data/out.txt"))
        ;

        Flow.runAsPipeline(flow);
    }
}
