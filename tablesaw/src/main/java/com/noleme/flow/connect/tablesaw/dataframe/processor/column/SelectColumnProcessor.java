package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

import java.util.Collection;

/**
 * Returns the output of a dataframe SELECT operation over a provided Table.
 *
 * @author Thomas Walter (twalter@lumiomedical.com)
 */
public class SelectColumnProcessor implements TableProcessor
{
    private final String[] selectedColumns;

    /**
     *
     * @param selectedColumns
     */
    public SelectColumnProcessor(String... selectedColumns)
    {
        this.selectedColumns = selectedColumns;
    }

    /**
     *
     * @param selectedColumns
     */
    public SelectColumnProcessor(Collection<String> selectedColumns)
    {
        this(selectedColumns.toArray(new String[0]));
    }

    @Override
    public Table process(Table table)
    {
        return table.select(this.selectedColumns);
    }
}
