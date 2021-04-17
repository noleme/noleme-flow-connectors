package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.Table;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/19
 */
public class TablesawForker implements Transformer<Table, Table>
{
    private final String name;

    private static final Logger logger = LoggerFactory.getLogger(TablesawForker.class);

    /**
     *
     */
    public TablesawForker()
    {
        this(null);
    }

    /**
     *
     * @param name
     */
    public TablesawForker(String name)
    {
        this.name = name;
    }

    @Override
    public Table transform(Table table)
    {
        var forkName = this.name == null ? table.name() : this.name;

        logger.info("Forking dataframe \"{}\" into \"{}\"...", table.name(), forkName);

        return table.copy().setName(forkName);
    }
}
