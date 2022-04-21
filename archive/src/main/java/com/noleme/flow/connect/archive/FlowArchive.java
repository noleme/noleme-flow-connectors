package com.noleme.flow.connect.archive;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 20/04/2022
 */
public final class FlowArchive
{
    private static final Logger logger = LoggerFactory.getLogger(FlowArchive.class);

    private FlowArchive() {}

    public static Transformer<InputStream, InputStream> unGzip()
    {
        return is -> {
            try {
                logger.info("Un-gzip of input stream");
                return new GZIPInputStream(is);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static Transformer<InputStream, InputStream> unTarGzip()
    {
        return is -> {
            try {
                logger.info("Un-tar-gzip of input stream");
                InputStream gzi = new GzipCompressorInputStream(is);
                return new TarArchiveInputStream(gzi);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }
}
