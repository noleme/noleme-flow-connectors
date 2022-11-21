package com.noleme.flow.connect.commons.transformer.filesystem;

import com.noleme.commons.file.Files;
import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/27
 */
public class FileStreamer implements Transformer<String, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(FileStreamer.class);

    @Override
    public InputStream transform(String path) throws FileNotFoundException
    {
        logger.info("Initializing stream from filesystem at {}", path);
        return Files.streamFrom(path);
    }
}
