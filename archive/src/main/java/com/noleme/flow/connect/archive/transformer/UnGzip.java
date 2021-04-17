package com.noleme.flow.connect.archive.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/24
 */
public class UnGzip implements Transformer<InputStream, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(UnGzip.class);

    @Override
    public InputStream transform(InputStream input) throws TransformationException
    {
        try {
            logger.info("Producing unzip input stream...");

            return new GzipCompressorInputStream(input);
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
