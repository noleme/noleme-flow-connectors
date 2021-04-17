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
 * The column target type can be different from the origin type (ie. column mutation is possible).
 * In order to do that, a column "provider" function is responsible for building target columns.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/17
 */
public class MapColumnIntoProcessor<T, U, C1 extends Column<T>, C2 extends Column<U>> implements TableProcessor
{
    private final Set<String> columnNames;
    private final ColumnDescriptor<T, C1> fromDescriptor;
    private final ColumnDescriptor<U, C2> toDescriptor;
    /* This is protected and not final so we can define it in subclasses while referencing class fields (not allowed within ctor) */
    protected Function<T, U> mapper;

    /**
     *
     * @param fromDescriptor
     * @param toDescriptor
     * @param mapper
     * @param columnNames
     */
    public MapColumnIntoProcessor(ColumnDescriptor<T, C1> fromDescriptor, ColumnDescriptor<U, C2> toDescriptor, Function<T, U> mapper, String... columnNames)
    {
        this(fromDescriptor, toDescriptor, mapper, Set.of(columnNames));
    }

    /**
     *
     * @param fromDescriptor
     * @param toDescriptor
     * @param mapper
     * @param columnNames
     */
    public MapColumnIntoProcessor(
        ColumnDescriptor<T, C1> fromDescriptor, ColumnDescriptor<U, C2> toDescriptor, Function<T, U> mapper,
        Collection<String> columnNames
    )
    {
        this.columnNames = new HashSet<>(columnNames);
        this.fromDescriptor = fromDescriptor;
        this.toDescriptor = toDescriptor;
        this.mapper = mapper;
    }

    @Override
    public Table process(Table table)
    {
        for (String name : this.columnNames)
        {
            table.replaceColumn(
                this.fromDescriptor.getColumn(table, name).mapInto(this.mapper, this.toDescriptor.createColumn(name, table.rowCount()))
            );
        }

        return table;
    }
}
