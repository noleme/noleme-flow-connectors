package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Adds an arbitrary number of columns of a given type to a given table.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/10
 */
public class AddColumnProcessor implements TableProcessor
{
    private final ColumnDescriptor<?, ?> columnDescriptor;
    private final List<String> names;

    /**
     *
     * @param columnDescriptor
     * @param names
     */
    public AddColumnProcessor(ColumnDescriptor<?, ?> columnDescriptor, String... names)
    {
        this.columnDescriptor = columnDescriptor;
        this.names = List.of(names);
    }

    /**
     *
     * @param columnDescriptor
     * @param names
     */
    public AddColumnProcessor(ColumnDescriptor<?, ?> columnDescriptor, Collection<String> names)
    {
        this.columnDescriptor = columnDescriptor;
        this.names = new ArrayList<>(names);
    }

    @Override
    public Table process(Table table)
    {
        Column<?>[] columns = this.names.stream()
            .map(name -> this.columnDescriptor.createColumn(name, table.rowCount()))
            .toArray(Column[]::new)
        ;

        return table.addColumns(columns);
    }
}
