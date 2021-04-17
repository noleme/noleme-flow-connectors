package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.StringColumn;

import java.util.Collection;
import java.util.function.Function;

/**
 * A MapColumnIntoProcessor shorthand that can map a Long column to a String column given a user-provided mapping function.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/07/29
 */
public class MapLongToStringProcessor extends MapColumnIntoProcessor<Long, String, LongColumn, StringColumn>
{
    /**
     *
     * @param mapper
     * @param columnNames
     */
    public MapLongToStringProcessor(Function<Long, String> mapper, String... columnNames)
    {
        super(ColumnDescriptor.LONG, ColumnDescriptor.STRING, mapper, columnNames);
    }

    /**
     *
     * @param mapper
     * @param columnNames
     */
    public MapLongToStringProcessor(Function<Long, String> mapper, Collection<String> columnNames)
    {
        super(ColumnDescriptor.LONG, ColumnDescriptor.STRING, mapper, columnNames);
    }
}
