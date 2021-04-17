package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Json;
import com.noleme.json.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/13
 */
public class ParseJsonArray implements Transformer<InputStream, ArrayNode>
{
    private static final Logger logger = LoggerFactory.getLogger(ParseJsonArray.class);

    @Override
    public ArrayNode transform(InputStream input) throws TransformationException
    {
        try {
            logger.info("Transforming input stream into a JSON array.");
            var json = Json.parse(input);

            if (!json.isArray())
                throw new TransformationException("The provided input could be parsed as JSON but doesn't seem to represent a JSON array.");

            return (ArrayNode) json;
        }
        catch (JsonException e) {
            throw new TransformationException("An error occurred while attempting to parse input as a JSON array.", e);
        }
    }
}
