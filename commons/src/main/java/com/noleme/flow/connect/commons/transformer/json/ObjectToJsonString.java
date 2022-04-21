package com.noleme.flow.connect.commons.transformer.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/08/21
 */
public class ObjectToJsonString<T> implements Transformer<T, String>
{
    private final boolean prettyPrint;

    private static final Logger logger = LoggerFactory.getLogger(ObjectToJsonString.class);

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
            logger.info("Serializing input into JSON.");

            ObjectWriter writer = Json.mapper().writer();

            if (this.prettyPrint)
                writer = writer.with(SerializationFeature.INDENT_OUTPUT);

            return writer.writeValueAsString(input);
        }
        catch (JsonProcessingException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
