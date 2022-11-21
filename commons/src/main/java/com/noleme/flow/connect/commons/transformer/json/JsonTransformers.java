package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.json.Json;
import com.noleme.json.JsonException;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/26
 */
@Deprecated
public final class JsonTransformers
{
    private JsonTransformers() {}

    /**
     *
     * @param input
     * @return
     * @throws TransformationException
     */
    public static JsonNode toJson(Object input) throws TransformationException
    {
        try {
            return Json.toJson(input);
        }
        catch (JsonException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
