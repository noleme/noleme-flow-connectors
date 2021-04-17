package com.noleme.flow.connect.etl;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/09
 */
public class ETLCompilationException extends ETLException
{
    /**
     *
     * @param message
     */
    public ETLCompilationException(String message)
    {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ETLCompilationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
