package com.noleme.flow.connect.tablesaw.loader;

import com.noleme.flow.actor.loader.Loader;
import com.noleme.flow.actor.loader.LoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/10
 */
public class TablesawCSVWrite implements Loader<Table>
{
    private final String path;
    private final Charset charset;

    private static final Logger logger = LoggerFactory.getLogger(TablesawCSVWrite.class);

    /**
     *
     * @param path
     */
    public TablesawCSVWrite(String path)
    {
        this(path, Charset.defaultCharset());
    }

    /**
     *
     * @param path
     * @param charset
     */
    public TablesawCSVWrite(String path, Charset charset)
    {
        this.path = path;
        this.charset = charset;
    }

    @Override
    public void load(Table table) throws LoadingException
    {
        try {
            logger.info("Loading table contents as CSV into {}", this.path);
            table.write().csv(new FileWriter(this.path, this.charset));
        }
        catch (IOException e) {
            throw new LoadingException("An unexpected error occurred while attempting to write table at " + this.path, e);
        }
    }
}
