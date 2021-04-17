package com.noleme.flow.connect.commons.transformer.iostream;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/07
 */
public class SkipLine implements Transformer<InputStream, InputStream>
{
    private final int skipCount;

    private static final Logger logger = LoggerFactory.getLogger(SkipLine.class);

    public SkipLine(int skipCount)
    {
        this.skipCount = skipCount;
    }

    public SkipLine()
    {
        this(1);
    }

    @Override
    public InputStream transform(InputStream input) throws TransformationException
    {
        try {
            for (int i = 0 ; i < skipCount ; ++i)
            {
                int read = input.read();
                while (read != -1 && (char)read != '\n')
                    read = input.read();
            }

            return input;
        }
        catch (IOException e) {
            logger.error("SkipLine tranformer encountered an error: "+e.getMessage(), e);
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
