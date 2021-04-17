package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Removes an arbitrary number of columns from a given table.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public class RemoveColumnProcessor implements TableProcessor
{
    private final Collection<String> removals;

    /**
     *
     * @param removals
     */
    public RemoveColumnProcessor(String... removals)
    {
        this(List.of(removals));
    }

    /**
     *
     * @param removals
     */
    public RemoveColumnProcessor(Collection<String> removals)
    {
        this.removals = removals;
    }

    @Override
    public Table process(Table table)
    {
        Set<String> nameDict = new HashSet<>(table.columnNames());

        this.removals.forEach(removal -> {
            if (!nameDict.contains(removal))
                return;
            table.removeColumns(table.column(removal));
            nameDict.remove(removal);
        });

        return table;
    }
}
