package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class TablePropertiesLoadingException extends Exception
{
    /**
     * @param message
     */
    public TablePropertiesLoadingException(String message)
    {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public TablePropertiesLoadingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
