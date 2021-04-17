package com.noleme.flow.connect.tablesaw.dataframe.processor.row;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

import java.util.Set;

/**
 *
 */
public class DropRowMissingProcessor implements TableProcessor
{
    private final Set<String> columnNames;

    /**
     *
     * @param columnNames
     */
    public DropRowMissingProcessor(String... columnNames)
    {
        this(Set.of(columnNames));
    }

    /**
     *
     * @param columnNames
     */
    public DropRowMissingProcessor(Set<String> columnNames)
    {
        this.columnNames = columnNames;
    }

    @Override
    public Table process(Table table)
    {
        for (String column : this.columnNames)
            table = table.where(t -> t.column(column).isNotMissing());

        return table;
    }
}
