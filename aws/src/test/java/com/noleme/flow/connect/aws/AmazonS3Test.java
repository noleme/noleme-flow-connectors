package com.noleme.flow.connect.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.noleme.flow.Flow;
import com.noleme.flow.connect.commons.generator.IterableGenerator;
import com.noleme.flow.connect.commons.transformer.iostream.InputStreamToString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Properties;

/**
 * @author Pierre Lecerf (pierre.lecerf@illuin.tech)
 */
public class AmazonS3Test
{
    private static LocalStackContainer localstack;
    private static AmazonS3 s3;

    private static final String bucketName = "test-bucket";

    @BeforeAll
    public static void before()
    {
        localstack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:0.11.3")
        ).withServices(Service.S3);
        localstack.start();

        var properties = new Properties();
        properties.setProperty("endpoint", localstack.getEndpointConfiguration(Service.S3).getServiceEndpoint());
        properties.setProperty("region", localstack.getRegion());
        properties.setProperty("access_key", localstack.getAccessKey());
        properties.setProperty("secret_key", localstack.getSecretKey());

        s3 = S3.buildS3(properties);

        s3.createBucket(bucketName);
        s3.putObject(bucketName, "obj0", "some_content_a");
        s3.putObject(bucketName, "obj1", "some_content_b");
        s3.putObject(bucketName, "obj2", "some_content_c");
    }

    @AfterAll
    public static void after()
    {
        localstack.close();
    }

    @Test
    public void testListing()
    {
        var flow = Flow.from(FlowS3.listObjects(s3, bucketName))
            .stream(IterableGenerator::new)
            .pipe(S3ObjectSummary::getKey)
            .accumulate()
            .collect()
        ;

        var out = Assertions.assertDoesNotThrow(() -> Flow.runAsPipeline(flow));
        List<String> expectedValues = List.of("obj0", "obj1", "obj2");

        Assertions.assertEquals(expectedValues, out.get(flow));
    }

    @Test
    public void testStream()
    {
        var flow = Flow.from(FlowS3.streamObject(s3, bucketName, "obj1"))
            .pipe(new InputStreamToString())
            .collect()
        ;

        var out = Assertions.assertDoesNotThrow(() -> Flow.runAsPipeline(flow));

        Assertions.assertEquals("some_content_b", out.get(flow));
    }
}
