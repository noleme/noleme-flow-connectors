package com.noleme.flow.connect.biteydf.vault.config;

import tech.bitey.dataframe.ColumnType;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/10/2021
 */
public enum ColumnTypes
{
    BOOLEAN(ColumnType.BOOLEAN),
    DATE(ColumnType.DATE),
    DATETIME(ColumnType.DATETIME),
    TIME(ColumnType.TIME),
    DOUBLE(ColumnType.DOUBLE),
    FLOAT(ColumnType.FLOAT),
    INT(ColumnType.INT),
    LONG(ColumnType.LONG),
    SHORT(ColumnType.SHORT),
    STRING(ColumnType.STRING),
    BYTE(ColumnType.BYTE),
    DECIMAL(ColumnType.DECIMAL),
    UUID(ColumnType.UUID),
    NSTRING(ColumnType.NSTRING)
    ;
    
    private final ColumnType<?> type;
    
    ColumnTypes(ColumnType<?> type)
    {
        this.type = type;
    }

    public ColumnType<?> getType()
    {
        return this.type;
    }
}
