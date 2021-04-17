package com.noleme.flow.connect.commons.transformer.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Json;
import com.noleme.json.JsonException;
import com.noleme.json.Yaml;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/21
 */
public class ObjectToYamlString<T> implements Transformer<T, String>
{
    private final boolean prettyPrint;

    public ObjectToYamlString()
    {
        this(false);
    }

    /**
     *
     * @param prettyPrint
     */
    public ObjectToYamlString(boolean prettyPrint)
    {
        this.prettyPrint = prettyPrint;
    }

    @Override
    public String transform(T input) throws TransformationException
    {
        try {
            JsonNode node = Yaml.toYaml(input);

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
