package com.noleme.flow.connect.http;

import com.noleme.flow.connect.commons.Slices;
import com.noleme.flow.connect.http.transformer.HttpStreamer;
import com.noleme.flow.slice.PipeSlice;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 04/04/2022
 */
public final class FlowHttp
{
    private FlowHttp() {}

    public static PipeSlice<URI, InputStream> streamOverHTTP(Supplier<HttpClient> supplier)
    {
        return Slices.sliceOf(upstream -> upstream
            .pipe(uri -> HttpRequest.newBuilder(uri).build())
            .pipe(new HttpStreamer(supplier))
            .pipe(HttpResponse::body)
        );
    }

    public static PipeSlice<URI, InputStream> streamOverHTTP()
    {
        return streamOverHTTP(HttpStreamer.defaultSupplier);
    }
}
