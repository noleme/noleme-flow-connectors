package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 10/06/2021
 */
public class TablesawCSVInputStream implements Transformer<Table, InputStream>
{
    private static final Logger logger = LoggerFactory.getLogger(TablesawCSVInputStream.class);

    @Override
    public InputStream transform(Table table)
    {
        logger.info("Transforming dataframe as CSV into stream");
        return new ByteArrayInputStream(table.write().toString().getBytes());
    }
}
