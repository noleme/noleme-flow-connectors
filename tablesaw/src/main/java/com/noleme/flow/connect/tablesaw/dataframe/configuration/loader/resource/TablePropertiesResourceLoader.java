package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.resource;

import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.commons.file.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class TablePropertiesResourceLoader implements TablePropertiesLoader<String>
{
    private final TablePropertiesLoader<InputStream> streamLoader;

    /**
     *
     * @param streamLoader
     */
    public TablePropertiesResourceLoader(TablePropertiesLoader<InputStream> streamLoader)
    {
        this.streamLoader = streamLoader;
    }

    @Override
    public TableProperties load(String path) throws TablePropertiesLoadingException
    {
        try {
            return this.streamLoader.load(Resources.streamFrom(path));
        }
        catch (IOException e) {
            throw new TablePropertiesLoadingException("No resource could be found at the provided path " + path + ".", e);
        }
    }
}
