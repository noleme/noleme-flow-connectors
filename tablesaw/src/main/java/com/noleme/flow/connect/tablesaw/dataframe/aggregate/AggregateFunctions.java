package com.noleme.flow.connect.tablesaw.dataframe.aggregate;

import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.columns.numbers.LongColumnType;
import tech.tablesaw.columns.strings.StringColumnType;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/07
 */
public final class AggregateFunctions
{
    private AggregateFunctions() {}

    public static final LongAggregateFunction firstLong = new LongAggregateFunction("First") {
        @Override
        public Long summarize(LongColumn column)
        {
            return column.isEmpty() ? LongColumnType.missingValueIndicator() : column.getLong(0);
        }
    };

    public static final StringAggregateFunction firstString = new StringAggregateFunction("First") {
        @Override
        public String summarize(StringColumn column)
        {
            return column.isEmpty() ? StringColumnType.missingValueIndicator() : column.getString(0);
        }
    };
}
