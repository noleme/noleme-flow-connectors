package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

/**
 * Adds a row index column
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/10
 */
public class AddColumnRowIndexProcessor implements TableProcessor
{
    @Override
    public Table process(Table table) throws TableProcessorException
    {
        if (table.columnNames().contains("index"))
            throw new TableProcessorException("Table "+table.name()+" already has an \"index\" column.");

        table.insertColumn(0, LongColumn.create("index", table.rowCount()));
        long index = 0;
        for (Row row : table)
            row.setLong("index", index++);

        return table;
    }
}
