package com.noleme.flow.connect.tablesaw.dataframe.processor.print;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class PrintTableProcessor implements TableProcessor
{
    private final int max;
    private final String[] columNames;

    /**
     *
     * @param columnNames
     */
    public PrintTableProcessor(String... columnNames)
    {
        this(20, columnNames);
    }

    /**
     *
     * @param max
     * @param columnNames
     */
    public PrintTableProcessor(int max, String... columnNames)
    {
        this.max = max;
        this.columNames = columnNames;
    }

    @Override
    public Table process(Table table)
    {
        Table selection = this.columNames.length == 0 ? table : table.select(this.columNames);

        System.out.println(this.max >= 0 ? selection.print(this.max) : selection.printAll());
        System.out.println("(row_count=" + table.rowCount() + ")");

        return table;
    }
}
