package com.noleme.flow.connect.tablesaw.loader;

import com.noleme.flow.actor.loader.Loader;
import com.noleme.flow.actor.loader.LoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.RuntimeIOException;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/10
 */
public class TablesawCSVWrite implements Loader<Table>
{
    private final String path;

    private static final Logger logger = LoggerFactory.getLogger(TablesawCSVWrite.class);

    /**
     *
     * @param path
     */
    public TablesawCSVWrite(String path)
    {
        this.path = path;
    }

    @Override
    public void load(Table table) throws LoadingException
    {
        try {
            logger.info("Loading dataframe as CSV into {}", this.path);
            table.write().toFile(this.path);
        }
        catch (RuntimeIOException e) {
            throw new LoadingException("An unexpected error occurred while attempting to write table at " + this.path, e);
        }
    }
}
