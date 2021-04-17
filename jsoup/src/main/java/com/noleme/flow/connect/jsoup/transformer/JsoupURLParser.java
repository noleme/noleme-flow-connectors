package com.noleme.flow.connect.jsoup.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/22
 */
public class JsoupURLParser implements Transformer<URL, Document>
{
    private final int timeout;

    private static final Logger logger = LoggerFactory.getLogger(JsoupURLParser.class);

    public JsoupURLParser(int timeout)
    {
        this.timeout = timeout;
    }

    public JsoupURLParser()
    {
        this(30000);
    }

    @Override
    public Document transform(URL url) throws TransformationException
    {
        try {
            logger.info("Initializing document from HTTP resource at {}", url.toString());
            return Jsoup.parse(url, this.timeout);
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
