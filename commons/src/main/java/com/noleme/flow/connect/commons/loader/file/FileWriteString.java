package com.noleme.flow.connect.commons.loader.file;

import com.noleme.commons.stream.Streams;
import com.noleme.flow.actor.loader.Loader;
import com.noleme.flow.actor.loader.LoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/01
 */
public class FileWriteString implements Loader<String>
{
    private final String outputFile;
    private final boolean append;

    private static final Logger logger = LoggerFactory.getLogger(FileWriteString.class);

    /**
     *
     * @param outputFile
     */
    public FileWriteString(String outputFile)
    {
        this(outputFile, false);
    }

    /**
     *
     * @param outputFile
     * @param append
     */
    public FileWriteString(String outputFile, boolean append)
    {
        this.outputFile = outputFile;
        this.append = append;
    }

    @Override
    public void load(String string) throws LoadingException
    {
        try {
            logger.info("Loading contents into {} (mode: {})", this.outputFile, (this.append ? "append" : "create"));
            Streams.flow(
                new ByteArrayInputStream(string.getBytes()),
                new FileOutputStream(this.outputFile, this.append)
            );
        }
        catch (IOException e) {
            throw new LoadingException("An error occurred while attempting to offload file at " + this.outputFile + ".", e);
        }
    }
}
