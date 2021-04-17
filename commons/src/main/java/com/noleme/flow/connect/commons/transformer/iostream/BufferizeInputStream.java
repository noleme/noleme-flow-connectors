package com.noleme.flow.connect.commons.transformer.iostream;

import com.noleme.flow.actor.transformer.Transformer;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/24
 */
public class BufferizeInputStream implements Transformer<InputStream, BufferedInputStream>
{
    @Override
    public BufferedInputStream transform(InputStream input)
    {
        return new BufferedInputStream(input);
    }
}
