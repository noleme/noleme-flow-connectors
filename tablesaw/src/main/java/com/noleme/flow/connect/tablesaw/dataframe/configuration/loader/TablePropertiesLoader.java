package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader;

import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public interface TablePropertiesLoader<I>
{
    /**
     *
     * @param input
     * @return
     * @throws TablePropertiesLoadingException
     */
    TableProperties load(I input) throws TablePropertiesLoadingException;

    /**
     *
     * @param loader
     * @param input
     * @param <I>
     * @return
     * @throws TablePropertiesLoadingException
     */
    static <I> TableProperties load(TablePropertiesLoader<I> loader, I input) throws TablePropertiesLoadingException
    {
        return loader.load(input);
    }
}
