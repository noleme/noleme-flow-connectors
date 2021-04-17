package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.tablesaw.dataframe.processor.print.PrintTableProcessor;
import tech.tablesaw.api.Table;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/10
 */
public class TablesawPrinter implements Transformer<Table, Table>
{
    private final PrintTableProcessor processor;

    /**
     *
     * @param columnNames
     */
    public TablesawPrinter(String... columnNames)
    {
        this(20, columnNames);
    }

    /**
     *
     * @param max
     * @param columnNames
     */
    public TablesawPrinter(int max, String... columnNames)
    {
        this.processor = new PrintTableProcessor(max, columnNames);
    }

    @Override
    public Table transform(Table input)
    {
        return this.processor.process(input);
    }
}
