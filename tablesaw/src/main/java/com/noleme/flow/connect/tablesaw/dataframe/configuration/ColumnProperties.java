package com.noleme.flow.connect.tablesaw.dataframe.configuration;

import tech.tablesaw.api.ColumnType;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/29
 */
public class ColumnProperties
{
    private final ColumnType type;
    private final String name;
    private final int sourceIndex;
    private final Integer targetIndex;

    /**
     *
     * @param type
     * @param name
     * @param sourceIndex
     * @param targetIndex
     */
    public ColumnProperties(ColumnType type, String name, int sourceIndex, Integer targetIndex)
    {
        this.type = type;
        this.name = name;
        this.sourceIndex = sourceIndex;
        this.targetIndex = targetIndex;
    }

    public ColumnType getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public int getSourceIndex()
    {
        return this.sourceIndex;
    }

    public Integer getTargetIndex()
    {
        return targetIndex;
    }
}
