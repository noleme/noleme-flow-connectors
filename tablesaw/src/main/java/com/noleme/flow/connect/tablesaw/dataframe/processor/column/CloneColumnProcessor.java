package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

/**
 * Adds a copy of a given column to a given table.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/06/22
 */
public class CloneColumnProcessor implements TableProcessor
{
    private final String fromColumn;
    private final String toColumn;

    /**
     *
     * @param fromColumn
     * @param toColumn
     */
    public CloneColumnProcessor(String fromColumn, String toColumn)
    {
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
    }

    @Override
    public Table process(Table table)
    {
        return table.addColumns(
            table.column(this.fromColumn).copy().setName(this.toColumn)
        );
    }
}
