package com.noleme.flow.connect.http.transformer;

import com.noleme.flow.actor.transformer.TransformationException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/24
 */
public final class HttpTransformers
{
    private HttpTransformers() {}

    /**
     *
     * @param url
     * @return
     * @throws TransformationException
     */
    public static URL asURL(String url) throws TransformationException
    {
        try {
            return new URL(url);
        }
        catch (MalformedURLException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
