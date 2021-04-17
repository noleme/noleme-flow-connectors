package com.noleme.nlp.data;

import java.util.List;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class Document
{
    private final String title;
    private final String text;
    private String cleanText;
    private List<Sentence> sentences;

    public Document(String title, String text)
    {
        this.title = title;
        this.text = text;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getText()
    {
        return this.text;
    }

    public String getCleanText()
    {
        return this.cleanText;
    }

    public Document setCleanText(String cleanText)
    {
        this.cleanText = cleanText;
        return this;
    }

    public List<Sentence> getSentences()
    {
        return this.sentences;
    }

    public Document setSentences(List<Sentence> sentences)
    {
        this.sentences = sentences;
        return this;
    }
}
