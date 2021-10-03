package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 19/09/2021
 */
public class MapMissingProcessor<T, C extends Column<T>> implements TableProcessor
{
    private final Set<String> columnNames;
    private final ColumnDescriptor<T, C> descriptor;
    /* This is protected and not final so we can define it in subclasses while referencing class fields/methods (which is not allowed within ctor) */
    protected Supplier<T> mapper;

    public MapMissingProcessor(ColumnDescriptor<T, C> descriptor, Supplier<T> mapper, String... columnNames)
    {
        this(descriptor, mapper, Set.of(columnNames));
    }

    /**
     *
     * @param descriptor
     * @param mapper
     * @param columnNames
     */
    public MapMissingProcessor(ColumnDescriptor<T, C> descriptor, Supplier<T> mapper, Collection<String> columnNames)
    {
        this.columnNames = new HashSet<>(columnNames);
        this.descriptor = descriptor;
        this.mapper = mapper;
    }

    @Override
    public Table process(Table table)
    {
        this.columnNames.forEach(name -> this.descriptor.getColumn(table, name).setMissingTo(this.mapper.get()));
        return table;
    }
}
