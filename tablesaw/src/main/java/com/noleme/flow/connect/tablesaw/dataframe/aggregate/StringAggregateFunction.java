package com.noleme.flow.connect.tablesaw.dataframe.aggregate;

import tech.tablesaw.aggregate.AggregateFunction;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.StringColumn;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/07
 */
public abstract class StringAggregateFunction extends AggregateFunction<StringColumn, String>
{
    public StringAggregateFunction(String name)
    {
        super(name);
    }

    @Override
    public boolean isCompatibleColumn(ColumnType type)
    {
        return type.equals(ColumnType.STRING);
    }

    @Override
    public ColumnType returnType()
    {
        return ColumnType.STRING;
    }
}
