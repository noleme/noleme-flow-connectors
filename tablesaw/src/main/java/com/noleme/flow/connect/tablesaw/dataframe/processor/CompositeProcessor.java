package com.noleme.flow.connect.tablesaw.dataframe.processor;

import com.noleme.commons.container.Lists;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public class CompositeProcessor implements TableProcessor
{
    private final List<TableProcessor> processors;

    public CompositeProcessor(TableProcessor... processors)
    {
        this.processors = Lists.of(new ArrayList<>(), processors);
    }

    @Override
    public Table process(Table table) throws TableProcessorException
    {
        for (TableProcessor processor : this.processors)
            table = processor.process(table);
        return table;
    }

    public CompositeProcessor addProcessor(TableProcessor processor)
    {
        this.processors.add(processor);
        return this;
    }
}
