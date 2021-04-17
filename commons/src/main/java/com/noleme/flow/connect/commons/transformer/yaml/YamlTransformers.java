package com.noleme.flow.connect.commons.transformer.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.json.JsonException;
import com.noleme.json.Yaml;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/26
 */
public final class YamlTransformers
{
    private YamlTransformers() {}

    /**
     *
     * @param input
     * @return
     * @throws TransformationException
     */
    public static JsonNode toYaml(Object input) throws TransformationException
    {
        try {
            return Yaml.toYaml(input);
        }
        catch (JsonException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
