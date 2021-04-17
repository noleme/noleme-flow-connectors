package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.connect.tablesaw.Tables;
import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import com.noleme.json.Json;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.util.Collection;
import java.util.List;

/**
 * This transformer will turn a series of column into a single JSON object stored in a new column.
 * This can be helpful when attempting to merge/append dataframes with different column schemas: we can compile extraneous columns into a single one if we need their contents down the line, and only retain columns that are in common.
 *
 * An optional "dropColumns" argument can be passed in order to require the removal of compiled columns.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/20
 */
public class CompileColumnsToJSONProcessor implements TableProcessor
{
    private final String columnName;
    private final Collection<String> columns;
    private final boolean dropColumns;

    public CompileColumnsToJSONProcessor(String columnName, Collection<String> columns)
    {
        this(columnName, false, columns);
    }

    public CompileColumnsToJSONProcessor(String columnName, String... columns)
    {
        this(columnName, false, List.of(columns));
    }

    public CompileColumnsToJSONProcessor(String columnName, boolean dropColumns, String... columns)
    {
        this(columnName, dropColumns, List.of(columns));
    }

    /**
     *
     * @param columnName
     * @param columns
     * @param dropColumns
     */
    public CompileColumnsToJSONProcessor(String columnName, boolean dropColumns, Collection<String> columns)
    {
        this.columnName = columnName;
        this.columns = columns;
        this.dropColumns = dropColumns;
    }

    @Override
    public Table process(Table table) throws TableProcessorException
    {
        table = Tables.addColumnIfNotExists(table, this.columnName, ColumnDescriptor.STRING);

        for (Row row : table)
        {
            ObjectNode object = row.isMissing(this.columnName)
                ? Json.newObject()
                : (ObjectNode) Json.parse(row.getString(this.columnName))
            ;

            for (String col : this.columns)
            {
                Object value;

                /* Extracted value is either null (in case of missing data) or we recover it using the appropriate ColumnDescriptor */
                if (row.isMissing(col))
                    value = null;
                else {
                    ColumnType type = row.getColumnType(col);
                    ColumnDescriptor<?, ?> descriptor = ColumnDescriptor.forType(type);

                    if (descriptor == null)
                        throw new TableProcessorException("The column " + col + " of type " + type.name() + " could not be mapped to a known ColumnDescriptor.");

                    value = descriptor.getValue(row, col);
                }

                object.set(col, Json.toJson(value));
            }

            row.setString(this.columnName, Json.stringify(object));
        }

        if (this.dropColumns)
            table = table.removeColumns(this.columns.stream().map(table::column).toArray(Column[]::new));

        return table;
    }
}
