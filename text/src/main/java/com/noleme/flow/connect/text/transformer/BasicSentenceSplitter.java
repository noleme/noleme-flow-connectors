package com.noleme.flow.connect.text.transformer;

import com.noleme.flow.actor.transformer.Transformer;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/17
 */
public class BasicSentenceSplitter implements Transformer<String, List<String>>
{
    private final Locale locale;

    /**
     *
     * @param locale
     */
    public BasicSentenceSplitter(Locale locale)
    {
        this.locale = locale;
    }

    @Override
    public List<String> transform(String input)
    {
        List<String> sentences = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(this.locale);
        iterator.setText(input);

        int start = iterator.first();
        int end = iterator.next();
        while (end != BreakIterator.DONE)
        {
            String sentence = input.substring(start, end).trim();

            if (sentence.isBlank())
                continue;

            sentences.add(sentence);

            start = end;
            end = iterator.next();
        }

        return sentences;
    }
}
