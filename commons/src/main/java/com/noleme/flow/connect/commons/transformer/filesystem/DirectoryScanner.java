package com.noleme.flow.connect.commons.transformer.filesystem;

import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 */
public class DirectoryScanner implements Transformer<String, Set<String>>
{
    private final Predicate<Path> filter;

    private static final Logger logger = LoggerFactory.getLogger(DirectoryScanner.class);

    public DirectoryScanner()
    {
        this(path -> true);
    }

    public DirectoryScanner(Predicate<Path> filter)
    {
        this.filter = filter;
    }

    @Override
    public Set<String> transform(String directory) throws IOException
    {
        try (Stream<Path> files = Files.list(Paths.get(directory)))
        {
            Set<String> paths = files
                .filter(f -> !Files.isDirectory(f))
                .filter(this.filter)
                .map(Path::toString)
                .collect(Collectors.toSet())
            ;

            logger.info("Scanning files in directory {} returned {} file(s)", directory, paths.size());

            return paths;
        }
    }
}
