package com.noleme.flow.connect.aws.extractor;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.noleme.flow.actor.extractor.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/26
 */
public class AmazonS3ListKeys implements Extractor<List<S3ObjectSummary>>
{
    private final AmazonS3 s3;
    private final String bucket;

    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ListKeys.class);

    /**
     *
     * @param s3
     * @param bucket
     */
    public AmazonS3ListKeys(AmazonS3 s3, String bucket)
    {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @Override
    public List<S3ObjectSummary> extract()
    {
        logger.info("Listing objects from S3 bucket {}", this.bucket);
        return this.s3.listObjects(this.bucket).getObjectSummaries();
    }
}
