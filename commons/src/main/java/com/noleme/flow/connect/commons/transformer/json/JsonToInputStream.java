package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/13
 */
public class JsonToInputStream<J extends JsonNode> implements Transformer<J, InputStream>
{
    private final boolean prettify;
    private final Charset charset;

    /**
     *
     */
    public JsonToInputStream()
    {
        this(false, Charset.defaultCharset());
    }

    /**
     *
     * @param prettify
     * @param charset
     */
    public JsonToInputStream(boolean prettify, Charset charset)
    {
        this.prettify = prettify;
        this.charset = charset;
    }

    @Override
    public InputStream transform(J json)
    {
        String jsonString = this.prettify ? Json.prettyPrint(json) : Json.stringify(json);
        return new ByteArrayInputStream(jsonString.getBytes(this.charset));
    }
}
