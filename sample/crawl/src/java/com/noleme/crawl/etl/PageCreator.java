package com.noleme.crawl.etl;

import com.noleme.crawl.data.Page;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/24
 */
public class PageCreator implements Transformer<InputStream, Page>
{
    private final Charset charset;

    public PageCreator(Charset charset)
    {
        this.charset = charset;
    }

    public PageCreator()
    {
        this(Charset.defaultCharset());
    }

    @Override
    public Page transform(InputStream input) throws TransformationException
    {
        return null;
    }
}
