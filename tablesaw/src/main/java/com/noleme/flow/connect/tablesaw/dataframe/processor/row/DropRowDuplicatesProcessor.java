package com.noleme.flow.connect.tablesaw.dataframe.processor.row;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

public class DropRowDuplicatesProcessor implements TableProcessor
{
    @Override
    public Table process(Table table)
    {
        return table.dropDuplicateRows();
    }
}
