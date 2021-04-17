package com.noleme.flow.connect.tablesaw.dataframe.column;

import tech.tablesaw.api.ColumnType;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/29
 */
public enum ColumnTypes
{
    SHORT(ColumnType.SHORT),
    INTEGER(ColumnType.INTEGER),
    LONG(ColumnType.LONG),
    FLOAT(ColumnType.FLOAT),
    BOOLEAN(ColumnType.BOOLEAN),
    STRING(ColumnType.STRING),
    DOUBLE(ColumnType.DOUBLE),
    LOCAL_DATE(ColumnType.LOCAL_DATE),
    LOCAL_TIME(ColumnType.LOCAL_TIME),
    LOCAL_DATE_TIME(ColumnType.LOCAL_DATE_TIME),
    INSTANT(ColumnType.INSTANT),
    TEXT(ColumnType.TEXT),
    SKIP(ColumnType.SKIP),
    ;

    private final ColumnType type;

    /**
     *
     * @param type
     */
    ColumnTypes(ColumnType type)
    {
        this.type = type;
    }

    public ColumnType getType()
    {
        return this.type;
    }
}
