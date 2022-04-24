package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.generator.Generator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/07
 */
public class BatchReaderGenerator implements Generator<List<String>>
{
    private final BufferedReader reader;
    private final int batchSize;
    private boolean hasNext;

    /**
     *
     * @param inputStream
     * @param batchSize
     */
    public BatchReaderGenerator(InputStream inputStream, int batchSize)
    {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.batchSize = batchSize;
        this.hasNext = true;
    }

    @Override
    public boolean hasNext()
    {
        return this.hasNext;
    }

    @Override
    public List<String> generate()
    {
        List<String> lines = this.reader.lines()
            .limit(this.batchSize)
            .collect(Collectors.toList())
        ;

        this.hasNext = lines.size() == this.batchSize;

        return lines;
    }
}
