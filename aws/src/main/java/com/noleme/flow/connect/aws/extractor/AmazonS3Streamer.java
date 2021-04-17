package com.noleme.flow.connect.aws.extractor;

import com.amazonaws.services.s3.AmazonS3;
import com.noleme.flow.actor.extractor.Extractor;
import com.noleme.flow.connect.aws.S3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public class AmazonS3Streamer implements Extractor<InputStream>
{
    private final AmazonS3 s3;
    private final String bucket;
    private final String fileName;

    private static final Logger logger = LoggerFactory.getLogger(AmazonS3Streamer.class);

    /**
     *
     * @param s3
     * @param bucket
     * @param fileName
     */
    public AmazonS3Streamer(AmazonS3 s3, String bucket, String fileName)
    {
        this.s3 = s3;
        this.bucket = bucket;
        this.fileName = fileName;
    }

    @Override
    public InputStream extract()
    {
        logger.info("Initializing stream from S3 bucket {} and key {}", this.bucket, this.fileName);
        return S3.getStream(this.s3, this.bucket, this.fileName);
    }
}
