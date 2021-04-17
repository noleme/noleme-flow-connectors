package com.noleme.flow.connect.http.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/17
 */
public class HttpStreamer implements Transformer<HttpRequest, HttpResponse<InputStream>>
{
    private final Supplier<HttpClient> supplier;

    private static final Logger logger = LoggerFactory.getLogger(HttpStreamer.class);

    /**
     *
     * @param supplier
     */
    public HttpStreamer(Supplier<HttpClient> supplier)
    {
        this.supplier = supplier;
    }

    /**
     *
     */
    public HttpStreamer()
    {
        this(() -> HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build());
    }

    @Override
    public HttpResponse<InputStream> transform(HttpRequest request) throws TransformationException
    {
        try {
            logger.info("Initializing stream from HTTP resource at {}", request.uri());

            HttpClient client = this.supplier.get();
            return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        }
        catch (InterruptedException | IOException e) {
            throw new TransformationException("An error occurred while attempting to send the request.", e);
        }
    }
}
