package com.noleme.flow.connect.archive.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/24
 */
public class UnGzipDecompressor implements Transformer<InputStream, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(UnGzipDecompressor.class);

    @Override
    public InputStream transform(InputStream input) throws TransformationException
    {
        try (InputStream gzi = new GzipCompressorInputStream(input))
        {
            var os = new ByteArrayOutputStream();

            logger.info("Unzipping input stream...");
            IOUtils.copy(gzi, os);

            return new ByteArrayInputStream(os.toByteArray());
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
