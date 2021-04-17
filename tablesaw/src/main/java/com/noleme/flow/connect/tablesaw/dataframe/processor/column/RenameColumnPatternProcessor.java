package com.noleme.flow.connect.tablesaw.dataframe.processor.column;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renames columns from a given table using pattern matching.
 * A mapping function can be provided to determine what to do with the match results.
 * By default, the mapping function will simply use the 1st matching group.
 *
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public class RenameColumnPatternProcessor implements TableProcessor
{
    private final Pattern pattern;
    private final BiFunction<String, Matcher, String> mapper;

    /**
     *
     * @param regex
     */
    public RenameColumnPatternProcessor(String regex)
    {
        this(regex, (name, matcher) -> matcher.group(1));
    }

    /**
     *
     * @param regex
     * @param mapper
     */
    public RenameColumnPatternProcessor(String regex, BiFunction<String, Matcher, String> mapper)
    {
        this.pattern = Pattern.compile(regex);
        this.mapper = mapper;
    }

    @Override
    public Table process(Table table)
    {
        for (String columName : table.columnNames())
        {
            Matcher matcher = this.pattern.matcher(columName);
            if (matcher.find())
                table.column(columName).setName(this.mapper.apply(columName, matcher));
        }

        return table;
    }
}
