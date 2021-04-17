package com.noleme.flow.connect.jsoup.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/22
 */
public class JsoupStreamParser implements Transformer<InputStream, Document>
{
    private final Charset charset;

    private static final Logger logger = LoggerFactory.getLogger(JsoupStreamParser.class);

    public JsoupStreamParser(Charset charset)
    {
        this.charset = charset;
    }

    public JsoupStreamParser()
    {
        this(Charset.defaultCharset());
    }

    @Override
    public Document transform(InputStream input) throws TransformationException
    {
        try {
            logger.info("Initializing document from stream");
            return Jsoup.parse(input, this.charset.displayName(), "");
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }
}
