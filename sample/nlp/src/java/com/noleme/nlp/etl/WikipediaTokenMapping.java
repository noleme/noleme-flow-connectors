package com.noleme.nlp.etl;

import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.nlp.data.Document;
import com.noleme.nlp.data.Sentence;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class WikipediaTokenMapping implements Transformer<Document, Document>
{
    private final Function<String, String> mapper;

    public WikipediaTokenMapping(Function<String, String> mapper)
    {
        this.mapper = mapper;
    }

    @Override
    public Document transform(Document document)
    {
        for (Sentence sentence : document.getSentences())
        {
            sentence.setTokens(sentence.getTokens().stream()
                .map(this.mapper)
                .collect(Collectors.toList())
            );
        }

        return document;
    }
}
