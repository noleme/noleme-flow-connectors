package com.noleme.flow.connect.tablesaw.dataframe.processor.string;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.Arrays;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/06/12
 */
public class StringConcatColumnProcessor implements TableProcessor
{
    private final String columnName;
    private final String[] concatColumnNames;

    /**
     *
     * @param columnName
     * @param concatColumnNames
     */
    public StringConcatColumnProcessor(String columnName, String... concatColumnNames)
    {
        this.columnName = columnName;
        this.concatColumnNames = concatColumnNames;
    }

    @Override
    public Table process(Table table)
    {
        Column<?>[] columns = Arrays.stream(this.concatColumnNames)
            .map(table::column)
            .toArray(Column[]::new)
        ;

        return table.replaceColumn(
            this.columnName,
            table.stringColumn(this.columnName).concatenate(columns).setName(this.columnName)
        );
    }
}
