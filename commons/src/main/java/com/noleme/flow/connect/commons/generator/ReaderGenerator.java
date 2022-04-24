package com.noleme.flow.connect.commons.generator;

import com.noleme.flow.actor.generator.GenerationException;
import com.noleme.flow.actor.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.noleme.flow.interruption.InterruptionException.interrupt;

/**
 * @author Pierre Lecerf (plecerf@noleme.com)
 * Created on 2020/12/17
 */
public class ReaderGenerator implements Generator<String>
{
    private final BufferedReader reader;
    private boolean hasNext;

    private static final Logger logger = LoggerFactory.getLogger(ReaderGenerator.class);

    public ReaderGenerator(InputStream stream)
    {
        this.reader = new BufferedReader(new InputStreamReader(stream));
        this.hasNext = true;
    }

    @Override
    public boolean hasNext()
    {
        return this.hasNext;
    }

    @Override
    public String generate() throws GenerationException
    {
        try {
            String line = this.reader.readLine();

            if (line == null)
            {
                this.hasNext = false;
                throw interrupt();
            }

            return line;
        }
        catch (IOException e) {
            logger.error("An error occurred while attempting to read a line from the stream: "+e.getMessage(), e);
            throw new GenerationException(e.getMessage(), e);
        }
    }
}
