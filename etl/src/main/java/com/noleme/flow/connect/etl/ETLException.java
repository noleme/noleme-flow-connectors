package com.noleme.flow.connect.etl;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/09
 */
@Deprecated
public class ETLException extends Exception
{
    /**
     *
     * @param message
     */
    public ETLException(String message)
    {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public ETLException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
