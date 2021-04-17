package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.function.Supplier;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/18
 */
public class ModifyColumnMissingProcessor <T, C extends Column<T>> implements TableProcessor
{
    private final ColumnDescriptor<T, C> descriptor;
    private final String columnName;
    private final Supplier<T> supplier;

    /**
     *
     * @param descriptor
     * @param columnName
     * @param value
     */
    public ModifyColumnMissingProcessor(ColumnDescriptor<T, C> descriptor, String columnName, T value)
    {
        this(descriptor, columnName, () -> value);
    }

    /**
     *
     * @param descriptor
     * @param columnName
     * @param supplier
     */
    public ModifyColumnMissingProcessor(ColumnDescriptor<T, C> descriptor, String columnName, Supplier<T> supplier)
    {
        this.descriptor = descriptor;
        this.columnName = columnName;
        this.supplier = supplier;
    }

    @Override
    public Table process(Table table) throws TableProcessorException
    {
        Column<T> column = this.descriptor.getColumn(table, this.columnName);
        column.setMissingTo(this.supplier.get());
        return table;
    }
}
