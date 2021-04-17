package com.noleme.nlp.etl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.nlp.data.Document;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class WikipediaDocumentCreator implements Transformer<ObjectNode, Document>
{
    @Override
    public Document transform(ObjectNode json) throws TransformationException
    {
        if (json.get("query").get("pages").has("-1"))
            throw new TransformationException("The provided JSON has no page contents.");

        JsonNode pages = json.get("query").get("pages");
        JsonNode result = pages.fields().next().getValue();

        String title = result.get("title").asText();
        String text = result.get("extract").asText();

        return new Document(title, text);
    }
}