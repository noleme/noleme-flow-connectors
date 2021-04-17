package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/14
 */
public class AddColumnFillProcessor<T> implements TableProcessor
{
    private final String name;
    private final ColumnDescriptor<T, ?> columnDescriptor;
    private final Function<Row, T> filler;

    /**
     *
     * @param columnDescriptor
     * @param name
     * @param filler
     */
    public AddColumnFillProcessor(ColumnDescriptor<T, ?> columnDescriptor, String name, Function<Row, T> filler)
    {
        this.columnDescriptor = columnDescriptor;
        this.name = name;
        this.filler = filler;
    }

    /**
     *
     * @param columnDescriptor
     * @param name
     * @param filler
     */
    public AddColumnFillProcessor(ColumnDescriptor<T, ?> columnDescriptor, String name, Supplier<T> filler)
    {
        this(columnDescriptor, name, (row) -> filler.get());
    }

    @Override
    public Table process(Table table)
    {
        table = table.addColumns(
            this.columnDescriptor.createColumn(this.name, table.rowCount())
        );

        for (Row row : table)
            this.columnDescriptor.setValue(row, this.name, this.filler.apply(row));

        return table;
    }
}
