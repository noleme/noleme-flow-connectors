package com.noleme.nlp.data;

import java.util.List;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class Sentence
{
    private final String text;
    private List<String> tokens;

    public Sentence(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }

    public List<String> getTokens()
    {
        return this.tokens;
    }

    public Sentence setTokens(List<String> tokens)
    {
        this.tokens = tokens;
        return this;
    }
}
