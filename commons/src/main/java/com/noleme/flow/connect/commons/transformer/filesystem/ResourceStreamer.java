package com.noleme.flow.connect.commons.transformer.filesystem;

import com.noleme.commons.file.Resources;
import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/27
 */
public class ResourceStreamer implements Transformer<String, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(ResourceStreamer.class);

    @Override
    public InputStream transform(String path) throws IOException
    {
        logger.info("Initializing stream from resources at {}", path);
        return Resources.streamFrom(path);
    }
}
