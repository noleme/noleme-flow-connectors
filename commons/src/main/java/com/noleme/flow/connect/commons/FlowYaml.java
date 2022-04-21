package com.noleme.flow.connect.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.commons.transformer.yaml.ParseYamlArray;
import com.noleme.flow.connect.commons.transformer.yaml.ParseYamlObject;
import com.noleme.json.Yaml;
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
public final class FlowYaml
{
    private static final Logger logger = LoggerFactory.getLogger(FlowYaml.class);
    
    private FlowYaml() {}

    public static <T> Transformer<T, String> instanceToYaml()
    {
        return instance -> {
            try {
                logger.info("Serializing instance as YAML");
                return Yaml.mapper().writeValueAsString(instance);
            }
            catch (JsonProcessingException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }
    
    public static <T, C extends Collection<T>> Transformer<C, String> collectionToYaml()
    {
        return collection -> {
            try {
                logger.info("Serializing collection as YAML");
                return Yaml.mapper().writeValueAsString(collection);
            }
            catch (JsonProcessingException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<String, T> yamlToInstance(Class<T> type)
    {
        return str -> {
            try {
                logger.info("Deserializing instance from YAML");
                return Yaml.mapper().readValue(str, type);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<String, List<T>> yamlToList(Class<T> type)
    {
        return str -> {
            try {
                logger.info("Deserializing collection from YAML");
                ObjectMapper mapper = Yaml.mapper();
                TypeFactory factory = mapper.getTypeFactory();
                CollectionType ct = factory.constructCollectionType(List.class, type);

                return mapper.readValue(str, ct);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<InputStream, T> yamlStreamToInstance(Class<T> type)
    {
        return is -> {
            try {
                logger.info("Deserializing instance from YAML");
                return Yaml.mapper().readValue(is, type);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static <T> Transformer<InputStream, List<T>> yamlStreamToList(Class<T> type)
    {
        return is -> {
            try {
                logger.info("Deserializing collection from YAML");
                ObjectMapper mapper = Yaml.mapper();
                TypeFactory factory = mapper.getTypeFactory();
                CollectionType ct = factory.constructCollectionType(List.class, type);
                
                return mapper.readValue(is, ct);
            }
            catch (IOException e) {
                throw new TransformationException(e.getMessage(), e);
            }
        };
    }

    public static Transformer<InputStream, ObjectNode> yamlStreamToObjectNode()
    {
        return new ParseYamlObject();
    }

    public static Transformer<InputStream, ArrayNode> yamlStreamToArrayNode()
    {
        return new ParseYamlArray();
    }
}
