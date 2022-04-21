package com.noleme.flow.connect.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.commons.transformer.json.ParseJsonArray;
import com.noleme.flow.connect.commons.transformer.json.ParseJsonObject;
import com.noleme.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 04/04/2022
 */
public final class FlowJson
{
    private static final Logger logger = LoggerFactory.getLogger(FlowJson.class);

    private FlowJson() {}

    public static <T> Transformer<T, String> instanceToJson()
    {
        return instance -> {
            try {
                logger.info("Serializing instance as JSON");
                return Json.mapper().writeValueAsString(instance);
            }
            catch (JsonProcessingException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T, C extends Collection<T>> Transformer<C, String> collectionToJson()
    {
        return collection -> {
            try {
                logger.info("Serializing collection as JSON");
                return Json.mapper().writeValueAsString(collection);
            }
            catch (JsonProcessingException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<String, T> jsonToInstance(Class<T> type)
    {
        return str -> {
            try {
                logger.info("Deserializing instance from JSON");
                return Json.mapper().readValue(str, type);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<String, List<T>> jsonToList(Class<T> type)
    {
        return str -> {
            try {
                logger.info("Deserializing collection from JSON");
                ObjectMapper mapper = Json.mapper();
                TypeFactory factory = mapper.getTypeFactory();
                CollectionType ct = factory.constructCollectionType(List.class, type);

                return mapper.readValue(str, ct);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<InputStream, T> jsonStreamToInstance(Class<T> type)
    {
        return is -> {
            try {
                logger.info("Deserializing instance from JSON");
                return Json.mapper().readValue(is, type);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<InputStream, List<T>> jsonStreamToList(Class<T> type)
    {
        return is -> {
            try {
                logger.info("Deserializing collection from JSON");
                ObjectMapper mapper = Json.mapper();
                TypeFactory factory = mapper.getTypeFactory();
                CollectionType ct = factory.constructCollectionType(List.class, type);

                return mapper.readValue(is, ct);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static Transformer<InputStream, ObjectNode> jsonStreamToObjectNode()
    {
        return new ParseJsonObject();
    }

    public static Transformer<InputStream, ArrayNode> jsonStreamToArrayNode()
    {
        return new ParseJsonArray();
    }
}
