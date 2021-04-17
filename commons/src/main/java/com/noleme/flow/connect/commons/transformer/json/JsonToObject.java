package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Json;
import com.noleme.json.JsonException;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/26
 */
public class JsonToObject <T> implements Transformer<JsonNode, T>
{
    private final Class<T> type;

    /**
     *
     * @param type
     */
    public JsonToObject(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public T transform(JsonNode input) throws TransformationException
    {
        try {
            return Json.fromJson(input, type);
        }
        catch (JsonException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
