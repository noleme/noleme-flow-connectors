package com.noleme.flow.connect.archive.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/24
 */
public class UnTarGzip implements Transformer<InputStream, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(UnTarGzip.class);

    @Override
    public InputStream transform(InputStream input) throws TransformationException
    {
        try {
            logger.info("Producing unzip compressed input stream...");

            InputStream gzi = new GzipCompressorInputStream(input);

            return new TarArchiveInputStream(gzi);
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
