package com.noleme.flow.connect.tablesaw;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/24
 */
public final class Tables
{
    /**
     * This is just a shorthand for checking the existence of a column in a table by its name.
     * The List.contains call is O(n) but since we're working on a Table we don't expect there to be more than a handful of dozen columns at the very most.
     * If you do have many more columns in your table, try a different approach ; this method is just here to cover the most common cases.
     *
     * @param table
     * @param name
     * @return
     */
    public static boolean hasColumn(Table table, String name)
    {
        return table.columnNames().contains(name);
    }

    /**
     *
     * @param table
     * @param name
     * @param descriptor
     * @return
     */
    public static <T, C extends Column<T>> boolean hasColumn(Table table, String name, ColumnDescriptor<?, C> descriptor)
    {
        if (!hasColumn(table, name))
            return false;

        var col = table.column(name);

        return descriptor.getColumnType().isAssignableFrom(col.getClass());
    }

    /**
     * Adds a column of a given type if it doesn't already exist in the provided table.
     * If no column exists with the specified name, we create one of the appropriate type.
     * Otherwise, we check if the existing one has the appropriate type, in which case we return it, otherwise we throw an exception.
     *
     * @param table
     * @param name
     * @param descriptor
     * @param <T>
     * @param <C>
     * @return
     */
    public static <T, C extends Column<T>> Table addColumnIfNotExists(Table table, String name, ColumnDescriptor<T, C> descriptor)
    {
        if (!hasColumn(table, name)) {
            return table.addColumns(
                descriptor.createColumn(name, table.rowCount())
            );
        }

        var col = table.column(name);

        /* Note that we throw an IllegalStateException mainly because this is what Tablesaw does with this kind of error */
        if (!descriptor.getColumnType().isAssignableFrom(col.getClass()))
            throw new IllegalStateException(
                "There already exists a column of name " + name + " in the provided table, but it is of a different type (" + col.getClass().getName() + ") from the one requested ("
                    + descriptor.getColumnType().getName() + ")."
            );

        return table;
    }
}
