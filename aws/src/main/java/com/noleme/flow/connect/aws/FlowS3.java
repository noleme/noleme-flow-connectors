package com.noleme.flow.connect.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.noleme.flow.actor.extractor.Extractor;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.aws.extractor.AmazonS3ListKeys;
import com.noleme.flow.connect.aws.transformer.AmazonS3Streamer;

import java.io.InputStream;
import java.util.List;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public final class FlowS3
{
    private FlowS3() {}

    public static Extractor<List<S3ObjectSummary>> listObjects(AmazonS3 s3, String bucket)
    {
        return new AmazonS3ListKeys(s3, bucket);
    }

    public static Extractor<InputStream> streamObject(AmazonS3 s3, String bucket, String filename)
    {
        return new AmazonS3Streamer(s3, bucket).asExtractor(filename);
    }

    public static Transformer<String, InputStream> streamObject(AmazonS3 s3, String bucket)
    {
        return new AmazonS3Streamer(s3, bucket);
    }
}
