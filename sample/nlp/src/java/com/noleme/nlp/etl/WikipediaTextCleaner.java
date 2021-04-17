package com.noleme.nlp.etl;

import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.nlp.data.Document;

import java.util.regex.Pattern;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class WikipediaTextCleaner implements Transformer<Document, Document>
{
    private final Pattern refPattern = Pattern.compile("(\\[\\d+])+");
    private final Pattern numberPattern = Pattern.compile("\\d?([\\,])\\d+");

    @Override
    public Document transform(Document document)
    {
        String cleaned = document.getText();

        cleaned = refPattern.matcher(cleaned).replaceAll("");
        cleaned = numberPattern.matcher(cleaned).replaceAll(result -> result.group(0).replaceAll(Pattern.quote(result.group(1)), ""));

        return document.setCleanText(cleaned);
    }
}
