package com.noleme.nlp.etl;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.text.transformer.BasicSentenceTokenizer;
import com.noleme.nlp.data.Document;
import com.noleme.nlp.data.Sentence;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class WikipediaSentenceTokenizer implements Transformer<Document, Document>
{
    private final BasicSentenceTokenizer tokenizer;

    public WikipediaSentenceTokenizer()
    {
        this.tokenizer = new BasicSentenceTokenizer();
    }

    @Override
    public Document transform(Document document) throws TransformationException
    {
        for (Sentence sentence : document.getSentences())
            sentence.setTokens(this.tokenizer.transform(sentence.getText()));

        return document;
    }
}