package com.noleme.flow.connect.tablesaw.dataframe.processor;

import tech.tablesaw.api.Table;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public interface TableProcessor
{
    /**
     *
     * @param table
     * @return
     * @throws TableProcessorException
     */
    Table process(Table table) throws TableProcessorException;
}
