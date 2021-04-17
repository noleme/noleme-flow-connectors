package com.noleme.flow.connect.tablesaw.dataframe.processor.string;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.column.MapColumnProcessor;
import tech.tablesaw.api.StringColumn;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/05/05
 */
public class StringReplaceProcessor extends MapColumnProcessor<String, StringColumn>
{
    private final Function<String, String> replacer;

    /**
     *
     * @param replacer
     * @param columnNames
     */
    public StringReplaceProcessor(Function<String, String> replacer, String... columnNames)
    {
        this(replacer, Set.of(columnNames));
    }

    /**
     *
     * @param replacer
     * @param columnNames
     */
    public StringReplaceProcessor(Function<String, String> replacer, Collection<String> columnNames)
    {
        super(ColumnDescriptor.STRING, null, columnNames);
        this.mapper = this::replace;
        this.replacer = replacer;
    }

    /**
     *
     * @param value
     * @return
     */
    private String replace(String value)
    {
        return this.replacer.apply(value);
    }
}
