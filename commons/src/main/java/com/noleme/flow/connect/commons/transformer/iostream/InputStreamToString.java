package com.noleme.flow.connect.commons.transformer.iostream;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.commons.stream.Streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/17
 */
public class InputStreamToString implements Transformer<InputStream, String>
{
    @Override
    public String transform(InputStream input) throws TransformationException
    {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Streams.flow(input, os);
            return os.toString();
        }
        catch (IOException e) {
            throw new TransformationException("An error occurred while attempting to read from the provided input stream.", e);
        }
    }
}
