package com.noleme.flow.connect.biteydf.transformer;

import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.biteydf.vault.config.TableProperties;
import tech.bitey.dataframe.DataFrame;

import java.io.InputStream;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/10/2021
 */
public abstract class BiteyDataframeParser implements Transformer<InputStream, DataFrame>
{
    protected final TableProperties properties;

    protected BiteyDataframeParser(TableProperties properties)
    {
        this.properties = properties;
    }

    protected abstract DataFrame postBuild(DataFrame table);
}
