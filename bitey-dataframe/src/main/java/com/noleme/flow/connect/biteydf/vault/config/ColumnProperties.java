package com.noleme.flow.connect.biteydf.vault.config;

import tech.bitey.dataframe.ColumnType;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 * Created on 2021/10/03
 */
public class ColumnProperties
{
    private ColumnType<?> type;
    private String name;
    private int index;
    private Integer targetIndex;

    public ColumnType<?> getType()
    {
        return this.type;
    }

    public ColumnProperties setType(String type)
    {
        this.type = ColumnTypes.valueOf(type).getType();
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public ColumnProperties setName(String name)
    {
        this.name = name;
        return this;
    }

    public int getIndex()
    {
        return this.index;
    }

    public ColumnProperties setIndex(int index)
    {
        this.index = index;
        return this;
    }

    public Integer getTargetIndex()
    {
        return this.targetIndex;
    }

    public ColumnProperties setTargetIndex(Integer targetIndex)
    {
        this.targetIndex = targetIndex;
        return this;
    }
}
