package com.noleme.flow.connect.commons.transformer.iostream;

import com.noleme.flow.actor.transformer.Transformer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/21
 */
public class StringToInputStream implements Transformer<String, InputStream>
{
    private final Charset charset;

    public StringToInputStream()
    {
        this.charset = Charset.defaultCharset();
    }

    /**
     *
     * @param charset
     */
    public StringToInputStream(Charset charset)
    {
        this.charset = charset;
    }

    @Override
    public InputStream transform(String input)
    {
        return new ByteArrayInputStream(input.getBytes(this.charset));
    }
}
