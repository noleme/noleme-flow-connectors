package com.noleme.flow.connect.tablesaw.dataframe.processor;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/10
 */
public class TableProcessorException extends Exception
{
    /**
     *
     * @param message
     */
    public TableProcessorException(String message)
    {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public TableProcessorException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
