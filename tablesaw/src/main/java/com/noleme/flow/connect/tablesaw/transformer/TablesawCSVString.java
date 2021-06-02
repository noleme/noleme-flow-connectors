package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 10/06/2021
 */
public class TablesawCSVString implements Transformer<Table, String>
{
    private static final Logger logger = LoggerFactory.getLogger(TablesawCSVString.class);

    @Override
    public String transform(Table table)
    {
        logger.info("Transforming dataframe as CSV into string");
        return table.write().toString();
    }
}
