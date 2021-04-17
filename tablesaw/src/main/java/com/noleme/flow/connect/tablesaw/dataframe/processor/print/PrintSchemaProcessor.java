package com.noleme.flow.connect.tablesaw.dataframe.processor.print;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.commons.string.Strings;
import tech.tablesaw.api.Table;

import java.util.StringJoiner;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class PrintSchemaProcessor implements TableProcessor
{
    @Override
    public Table process(Table table)
    {
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("table:        " + table.name());
        joiner.add("column_count: " + table.columnNames().size());
        joiner.add("row_count:    " + table.rowCount());
        joiner.add("columns:");

        int index = 0;
        for (String col : table.columnNames())
        {
            String type = table.column(col).type().name();
            joiner.add(" " + Strings.padRight("" + index, 4) + " " + Strings.padRight(type, 16) + " " + col);
            index++;
        }

        System.out.println(joiner);

        return table;
    }
}
