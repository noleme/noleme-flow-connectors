package com.noleme.flow.connect.commons.transformer.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.json.Yaml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/04/13
 */
public class YamlToInputStream<Y extends JsonNode> implements Transformer<Y, InputStream>
{
    private final boolean prettify;
    private final Charset charset;

    /**
     *
     */
    public YamlToInputStream()
    {
        this(false, Charset.defaultCharset());
    }

    /**
     *
     * @param prettify
     * @param charset
     */
    public YamlToInputStream(boolean prettify, Charset charset)
    {
        this.prettify = prettify;
        this.charset = charset;
    }

    @Override
    public InputStream transform(Y yaml)
    {
        String yamlString = this.prettify ? Yaml.prettyPrint(yaml) : Yaml.stringify(yaml);
        return new ByteArrayInputStream(yamlString.getBytes(this.charset));
    }
}
