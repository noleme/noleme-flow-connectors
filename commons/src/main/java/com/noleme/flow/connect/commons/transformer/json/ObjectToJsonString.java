package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Json;
import com.noleme.json.JsonException;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/21
 */
public class ObjectToJsonString<T> implements Transformer<T, String>
{
    private final boolean prettyPrint;

    public ObjectToJsonString()
    {
        this(false);
    }

    /**
     *
     * @param prettyPrint
     */
    public ObjectToJsonString(boolean prettyPrint)
    {
        this.prettyPrint = prettyPrint;
    }

    @Override
    public String transform(T input) throws TransformationException
    {
        try {
            JsonNode node = Json.toJson(input);

            return this.prettyPrint
                ? Json.prettyPrint(node)
                : Json.stringify(node)
            ;
        }
        catch (JsonException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
