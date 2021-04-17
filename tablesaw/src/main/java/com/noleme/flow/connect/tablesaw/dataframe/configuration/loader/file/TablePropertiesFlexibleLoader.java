package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.file;

import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.commons.file.Files;
import com.noleme.commons.file.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class TablePropertiesFlexibleLoader implements TablePropertiesLoader<String>
{
    private final TablePropertiesLoader<InputStream> streamLoader;

    /**
     *
     * @param streamLoader
     */
    public TablePropertiesFlexibleLoader(TablePropertiesLoader<InputStream> streamLoader)
    {
        this.streamLoader = streamLoader;
    }

    @Override
    public TableProperties load(String path) throws TablePropertiesLoadingException
    {
        try {
            if (Files.fileExists(path))
                return this.streamLoader.load(Files.streamFrom(path));
            else if (Resources.exists(path))
                return this.streamLoader.load(Resources.streamFrom(path));

            throw new IOException("No file or resource could be found at the provided path " + path);
        }
        catch (IOException e) {
            throw new TablePropertiesLoadingException("No file or resource could be found at the provided path " + path + ".", e);
        }
    }
}
