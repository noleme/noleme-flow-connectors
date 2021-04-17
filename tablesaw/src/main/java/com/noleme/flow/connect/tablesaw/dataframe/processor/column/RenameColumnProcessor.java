package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import tech.tablesaw.api.Table;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.noleme.commons.function.RethrowBiConsumer.rethrower;

/**
 * Renames an arbitrary number of columns from a given table using a name map.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public class RenameColumnProcessor implements TableProcessor
{
    private final Map<String, String> mapping;

    /**
     *
     * @param from
     * @param to
     */
    public RenameColumnProcessor(String from, String to)
    {
        this(Map.of(from, to));
    }

    /**
     *
     * @param mapping
     */
    public RenameColumnProcessor(Map<String, String> mapping)
    {
        this.mapping = mapping;
    }

    @Override
    public Table process(Table table) throws TableProcessorException
    {
        Set<String> nameDict = new HashSet<>(table.columnNames());

        /* We need to perform a table copy because altering names is done "in place" */
        Table clone = table.copy();

        this.mapping.forEach(rethrower((oldName, newName) -> {
            if (!nameDict.contains(oldName))
                throw new TableProcessorException("No column of name " + oldName + " could be found in table " + clone.name());
            clone.column(oldName).setName(newName);
        }));

        return clone;
    }
}
