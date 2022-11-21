package com.noleme.flow.connect.commons.transformer.filesystem;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/22
 */
public class CreateDirectory<I> implements Transformer<I, I>
{
    private final Function<I, Path> pathCreator;

    private static final Logger logger = LoggerFactory.getLogger(CreateDirectory.class);

    public CreateDirectory(Path path)
    {
        this.pathCreator = any -> path;
    }

    public CreateDirectory(String path)
    {
        this(Path.of(path));
    }

    public CreateDirectory(Function<I, Path> pathCreator)
    {
        this.pathCreator = pathCreator;
    }

    @Override
    public I transform(I input) throws TransformationException
    {
        try {
            Path path = this.pathCreator.apply(input);

            if (!Files.exists(path))
            {
                logger.info("Initializing stream from filesystem at {}", path);
                Files.createDirectory(path);
            }
            else if (Files.isDirectory(path))
                logger.info("Directory already exists at {}", path);
            else
                logger.info("A file already exists at {}", path);

            return input;
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
