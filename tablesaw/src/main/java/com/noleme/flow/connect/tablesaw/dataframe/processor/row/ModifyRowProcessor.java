package com.noleme.flow.connect.tablesaw.dataframe.processor.row;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.util.function.Consumer;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/14
 */
public class ModifyRowProcessor implements TableProcessor
{
    private final Consumer<Row> modifier;

    /**
     *
     * @param modifier
     */
    public ModifyRowProcessor(Consumer<Row> modifier)
    {
        this.modifier = modifier;
    }

    @Override
    public Table process(Table table)
    {
        for (Row row : table)
            this.modifier.accept(row);

        return table;
    }
}
