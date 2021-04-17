package com.noleme.flow.connect.tablesaw.dataframe.processor.print;

import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import tech.tablesaw.api.Table;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/27
 */
public class PrintStructureProcessor implements TableProcessor
{
    @Override
    public Table process(Table table)
    {
        System.out.println(table.structure().printAll());

        return table;
    }
}
