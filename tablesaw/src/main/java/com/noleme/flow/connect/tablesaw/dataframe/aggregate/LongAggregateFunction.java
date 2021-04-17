package com.noleme.flow.connect.tablesaw.dataframe.aggregate;

import tech.tablesaw.aggregate.AggregateFunction;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.LongColumn;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/07
 */
public abstract class LongAggregateFunction extends AggregateFunction<LongColumn, Long>
{
    public LongAggregateFunction(String name)
    {
        super(name);
    }

    @Override
    public boolean isCompatibleColumn(ColumnType type)
    {
        return type.equals(ColumnType.LONG);
    }

    @Override
    public ColumnType returnType()
    {
        return ColumnType.LONG;
    }
}
