package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.iostream;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.json.TablePropertiesNodeLoader;
import com.noleme.json.Json;

import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/24
 */
public class TablePropertiesJSONStreamLoader implements TablePropertiesLoader<InputStream>
{
    private final TablePropertiesLoader<ObjectNode> jsonLoader = new TablePropertiesNodeLoader();

    @Override
    public TableProperties load(InputStream input) throws TablePropertiesLoadingException
    {
        ObjectNode json = (ObjectNode) Json.parse(input);

        return this.jsonLoader.load(json);
    }
}
