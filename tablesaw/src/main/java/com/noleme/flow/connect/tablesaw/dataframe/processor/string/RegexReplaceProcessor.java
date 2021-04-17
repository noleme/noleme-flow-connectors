package com.noleme.flow.connect.tablesaw.dataframe.processor.string;

import com.noleme.flow.connect.tablesaw.dataframe.descriptor.ColumnDescriptor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.column.MapColumnProcessor;
import tech.tablesaw.api.StringColumn;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/17
 */
public class RegexReplaceProcessor extends MapColumnProcessor<String, StringColumn>
{
    private final Pattern pattern;
    private final Function<MatchResult, String> replacer;

    /**
     *
     * @param pattern
     * @param columnNames
     */
    public RegexReplaceProcessor(String pattern, Function<MatchResult, String> replacer, String... columnNames)
    {
        this(pattern, replacer, Set.of(columnNames));
    }

    /**
     *
     * @param pattern
     * @param replacer
     * @param columnNames
     */
    public RegexReplaceProcessor(String pattern, Function<MatchResult, String> replacer, Collection<String> columnNames)
    {
        super(ColumnDescriptor.STRING, null, columnNames);
        this.mapper = this::searchAndReplace;
        this.pattern = Pattern.compile(pattern);
        this.replacer = replacer;
    }

    /**
     *
     * @param value
     * @return
     */
    private String searchAndReplace(String value)
    {
        Matcher matcher = this.pattern.matcher(value);
        return matcher.find()
            ? matcher.replaceAll(this.replacer)
            : value
        ;
    }
}
