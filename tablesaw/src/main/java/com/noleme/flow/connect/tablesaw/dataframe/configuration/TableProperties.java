package com.noleme.flow.connect.tablesaw.dataframe.configuration;

import tech.tablesaw.api.ColumnType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class TableProperties
{
    private String name;
    private Character separator;
    private Character quoteChar;
    private int maxCharsPerColumn;
    private Charset charset;
    private boolean hasHeader;
    private int columnCount;
    private int sampleSize;
    private boolean addRowIndex;
    private List<ColumnProperties> mapping;

    public TableProperties()
    {
        this.name = null;
        this.separator = ',';
        this.quoteChar = '"';
        this.maxCharsPerColumn = 4096;
        this.charset = Charset.defaultCharset();
        this.hasHeader = true;
        this.sampleSize = -1;
        this.addRowIndex = true;
        this.mapping = new ArrayList<>();
    }

    public String getName()
    {
        return this.name;
    }

    public TableProperties setName(String name)
    {
        this.name = name;
        return this;
    }

    public Character getSeparator()
    {
        return separator;
    }

    public TableProperties setSeparator(Character separator)
    {
        this.separator = separator;
        return this;
    }

    public Character getQuoteChar()
    {
        return quoteChar;
    }

    public TableProperties setQuoteChar(Character quoteChar)
    {
        this.quoteChar = quoteChar;
        return this;
    }

    public int getMaxCharsPerColumn()
    {
        return this.maxCharsPerColumn;
    }

    public TableProperties setMaxCharsPerColumn(int max)
    {
        this.maxCharsPerColumn = max;
        return this;
    }

    public Charset getCharset()
    {
        return this.charset;
    }

    public TableProperties setCharset(Charset charset)
    {
        this.charset = charset;
        return this;
    }

    public boolean hasHeader()
    {
        return hasHeader;
    }

    public TableProperties setHasHeader(boolean hasHeader)
    {
        this.hasHeader = hasHeader;
        return this;
    }

    public int getColumnCount()
    {
        return columnCount;
    }

    public TableProperties setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
        return this;
    }

    public int getSampleSize()
    {
        return sampleSize;
    }

    public TableProperties setSampleSize(int sampleSize)
    {
        this.sampleSize = sampleSize;
        return this;
    }

    public boolean requiresRowIndex()
    {
        return addRowIndex;
    }

    public TableProperties setAddRowIndex(boolean addRowIndex)
    {
        this.addRowIndex = addRowIndex;
        return this;
    }

    public List<ColumnProperties> getMapping()
    {
        return mapping;
    }

    public List<ColumnProperties> getActiveMapping()
    {
        return mapping.stream().filter(cp -> cp.getType() != ColumnType.SKIP).collect(Collectors.toList());
    }

    public TableProperties setMapping(List<ColumnProperties> mapping)
    {
        this.mapping = mapping;
        return this;
    }
}
