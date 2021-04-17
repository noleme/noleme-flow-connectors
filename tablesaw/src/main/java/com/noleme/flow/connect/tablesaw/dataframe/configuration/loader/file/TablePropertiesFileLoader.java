package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.file;

import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.commons.file.Files;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class TablePropertiesFileLoader implements TablePropertiesLoader<String>
{
    private final TablePropertiesLoader<InputStream> streamLoader;

    /**
     *
     * @param streamLoader
     */
    public TablePropertiesFileLoader(TablePropertiesLoader<InputStream> streamLoader)
    {
        this.streamLoader = streamLoader;
    }

    @Override
    public TableProperties load(String path) throws TablePropertiesLoadingException
    {
        try {
            return this.streamLoader.load(Files.streamFrom(path));
        }
        catch (FileNotFoundException e) {
            throw new TablePropertiesLoadingException("No file could be found at the provided path " + path + ".", e);
        }
    }
}
