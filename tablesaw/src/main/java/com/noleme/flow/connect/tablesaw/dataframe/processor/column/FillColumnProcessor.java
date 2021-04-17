package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.function.Supplier;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/21
 */
public class FillColumnProcessor <T> implements TableProcessor
{
    private final String columnName;
    private final ColumnDescriptor<T, ?> descriptor;
    private final Supplier<T> supplier;

    /**
     *
     * @param descriptor
     * @param columnName
     * @param value
     */
    public FillColumnProcessor(ColumnDescriptor<T, ?> descriptor, String columnName, T value)
    {
        this(descriptor, columnName, () -> value);
    }

    /**
     *
     * @param descriptor
     * @param columnName
     * @param supplier
     */
    public FillColumnProcessor(ColumnDescriptor<T, ?> descriptor, String columnName, Supplier<T> supplier)
    {
        this.columnName = columnName;
        this.descriptor = descriptor;
        this.supplier = supplier;
    }

    @Override
    public Table process(Table table)
    {
        Column<T> newColumn = this.descriptor.getColumn(table, this.columnName)
            .emptyCopy(table.rowCount())
            .setMissingTo(this.supplier.get())
        ;

        return table.replaceColumn(newColumn);
    }
}
