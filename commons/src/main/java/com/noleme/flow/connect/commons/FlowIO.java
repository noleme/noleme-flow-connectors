package com.noleme.flow.connect.commons;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.commons.transformer.filesystem.DirectoryScanner;
import com.noleme.flow.connect.commons.transformer.filesystem.FileStreamer;
import com.noleme.flow.connect.commons.transformer.filesystem.FlexibleStreamer;
import com.noleme.flow.connect.commons.transformer.filesystem.ResourceStreamer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public final class FlowIO
{
    private static final FileStreamer fileStreamer = new FileStreamer();
    private static final ResourceStreamer resourceStreamer = new ResourceStreamer();
    private static final FlexibleStreamer flexibleStreamer = new FlexibleStreamer();
    private static final DirectoryScanner directoryScanner = new DirectoryScanner();

    private FlowIO() {}

    public static InputStream streamFile(String path) throws FileNotFoundException
    {
        return fileStreamer.transform(path);
    }

    public static InputStream streamResource(String path) throws IOException
    {
        return resourceStreamer.transform(path);
    }

    public static InputStream streamFileOrResource(String path) throws IOException, TransformationException
    {
        return flexibleStreamer.transform(path);
    }

    public static Set<String> listFiles(String directoryPath) throws IOException
    {
        return directoryScanner.transform(directoryPath);
    }

    public static Transformer<String, Set<String>> listFiles(Predicate<Path> filter)
    {
        return new DirectoryScanner(filter);
    }
}
