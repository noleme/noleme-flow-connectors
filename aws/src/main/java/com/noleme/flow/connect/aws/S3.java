package com.noleme.flow.connect.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.noleme.commons.file.Files;
import com.noleme.commons.stream.Streams;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Small helper class for S3-based common operations
 */
public final class S3
{
    private S3() {}

    /**
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(String path) throws IOException
    {
        Properties properties = new Properties();
        properties.load(Files.streamFrom(path));
        return properties;
    }

    /**
     *
     * @param properties
     * @return
     */
    public static AmazonS3 buildS3(Properties properties)
    {
        var builder = AmazonS3ClientBuilder.standard();

        if (properties.containsKey("endpoint") && properties.containsKey("region"))
        {
            builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                properties.getProperty("endpoint"),
                properties.getProperty("region")
            ));
        }
        else if (properties.containsKey("region"))
            builder.withRegion(Regions.fromName(properties.getProperty("region")));

        if (properties.containsKey("access_key"))
        {
            AWSCredentials credentials = new BasicAWSCredentials(
                properties.getProperty("access_key"),
                properties.getProperty("secret_key")
            );

            builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        }

        var config = new ClientConfiguration();
        config.setSocketTimeout(0);
        config.withMaxConnections(1024);

        return builder
            .withClientConfiguration(config)
            .build()
        ;
    }

    /**
     *
     * @param conf
     * @return
     * @throws IOException
     */
    public static AmazonS3 buildS3(String conf) throws IOException
    {
        return buildS3(loadProperties(conf));
    }

    /**
     *
     * @param accessKey
     * @param secretKey
     * @param region
     * @return
     */
    public static AmazonS3 buildS3(String accessKey, String secretKey, String region)
    {
        var properties = new Properties();
        properties.setProperty("access_key", accessKey);
        properties.setProperty("secret_key", secretKey);
        properties.setProperty("region", region);

        return buildS3(properties);
    }

    /**
     *
     * @param s3
     * @param bucket
     * @param fileName
     * @return
     */
    public static InputStream getStream(AmazonS3 s3, String bucket, String fileName)
    {
        S3Object s3object = s3.getObject(bucket, fileName);
        return s3object.getObjectContent();
    }

    /**
     *
     * @param s3
     * @param bucket
     * @param source
     * @param target
     * @throws IOException
     */
    public static void download(AmazonS3 s3, String bucket, String source, String target) throws IOException
    {
        Streams.flow(getStream(s3, bucket, source), new FileOutputStream(target));
    }

    /**
     *
     * @param s3
     * @param target
     * @throws IOException
     */
    public static void download(AmazonS3 s3, DownloadTarget target) throws IOException
    {
        Streams.flow(getStream(s3, target.bucket, target.key), new FileOutputStream(target.localPath));
    }

    /**
     *
     * @param s3
     * @param bucket
     * @param source
     * @param target
     * @throws FileNotFoundException
     */
    public static void upload(AmazonS3 s3, String bucket, String source, String target) throws FileNotFoundException
    {
        s3.putObject(bucket, target, Files.streamFrom(source), new ObjectMetadata());
    }

    /**
     *
     */
    public static class DownloadTarget
    {
        public final String bucket;
        public final String key;
        public final String name;
        public final String localPath;
        public final String extension;

        /**
         *
         * @param bucket
         * @param key
         */
        public DownloadTarget(String bucket, String key)
        {
            this.bucket = bucket;
            this.key = key;
            this.name = key.substring(key.lastIndexOf('/') + 1);
            this.localPath = "out/" + name;
            int extIndex = this.name.indexOf('.');
            this.extension = extIndex >= 0 ? this.name.substring(extIndex + 1) : null;
        }
    }
}
