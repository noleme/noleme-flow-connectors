package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Transforms the values in a given set of columns using a provided mapping function.
 * The column target type is expected to be the same as the origin type (ie. column mutation is not possible).
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/17
 */
public class MapColumnProcessor<T, C extends Column<T>> implements TableProcessor
{
    private final Set<String> columnNames;
    private final ColumnDescriptor<T, C> descriptor;
    /* This is protected and not final so we can define it in subclasses while referencing class fields/methods (which is not allowed within ctor) */
    protected Function<T, T> mapper;

    /**
     *
     * @param descriptor
     * @param mapper
     * @param columnNames
     */
    public MapColumnProcessor(ColumnDescriptor<T, C> descriptor, Function<T, T> mapper, String... columnNames)
    {
        this(descriptor, mapper, Set.of(columnNames));
    }

    /**
     *
     * @param descriptor
     * @param mapper
     * @param columnNames
     */
    public MapColumnProcessor(ColumnDescriptor<T, C> descriptor, Function<T, T> mapper, Collection<String> columnNames)
    {
        this.columnNames = new HashSet<>(columnNames);
        this.descriptor = descriptor;
        this.mapper = mapper;
    }

    @Override
    public Table process(Table table)
    {
        this.columnNames.forEach(
            name -> table.replaceColumn(
                this.descriptor.getColumn(table, name).map(this.mapper)
            )
        );

        return table;
    }
}
