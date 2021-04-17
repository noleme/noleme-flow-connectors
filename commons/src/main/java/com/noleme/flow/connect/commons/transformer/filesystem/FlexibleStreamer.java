package com.noleme.flow.connect.commons.transformer.filesystem;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.commons.file.Files;
import com.noleme.commons.file.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/05/26
 */
public class FlexibleStreamer implements Transformer<String, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(FlexibleStreamer.class);

    @Override
    public InputStream transform(String path) throws TransformationException
    {
        try {
            if (Files.fileExists(path))
            {
                logger.info("Initializing stream from filesystem at {}", path);
                return Files.streamFrom(path);
            }

            if (Resources.exists(path))
            {
                logger.info("Initializing stream from resources at {}", path);
                return Resources.streamFrom(path);
            }

            throw new TransformationException("No file nor resource could be found at path " + path);
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
