package com.noleme.flow.connect.commons.transformer.yaml;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.JsonException;
import com.noleme.json.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/13
 */
public class ParseYamlObject implements Transformer<InputStream, ObjectNode>
{
    private static final Logger logger = LoggerFactory.getLogger(ParseYamlObject.class);

    @Override
    public ObjectNode transform(InputStream input) throws TransformationException
    {
        try {
            logger.info("Transforming input stream into a YAML object.");
            var yaml = Yaml.parse(input);

            if (!yaml.isObject())
                throw new TransformationException("The provided input could be parsed as YAML but doesn't seem to represent a YAML object.");

            return (ObjectNode) yaml;
        }
        catch (JsonException e) {
            throw new TransformationException("An error occurred while attempting to parse input as a YAML object.", e);
        }
    }
}
