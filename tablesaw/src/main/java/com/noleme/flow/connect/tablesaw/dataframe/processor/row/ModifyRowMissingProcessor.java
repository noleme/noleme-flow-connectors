package com.noleme.flow.connect.tablesaw.dataframe.processor.row;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.util.function.Consumer;

/**
 * Transforms any row with missing values using a Consumer that has access to the whole Row.
 * This can be useful for "emulating" values using other columns of the table.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/07/28
 */
public class ModifyRowMissingProcessor implements TableProcessor
{
    private final Consumer<Row> modifier;
    private final String column;

    /**
     *
     * @param column
     * @param modifier
     */
    public ModifyRowMissingProcessor(String column, Consumer<Row> modifier)
    {
        this.modifier = modifier;
        this.column = column;
    }

    @Override
    public Table process(Table table)
    {
        var missing = table.where(t -> t.column(this.column).isMissing());

        if (missing.isEmpty())
            return table;

        for (Row row : missing)
            this.modifier.accept(row);

        return table
            .where(t -> t.column(this.column).isNotMissing())
            .append(missing);
    }
}
