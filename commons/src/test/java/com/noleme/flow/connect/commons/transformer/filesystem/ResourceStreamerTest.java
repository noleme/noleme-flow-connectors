package com.noleme.flow.connect.commons.transformer.filesystem;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.transformer.iostream.InputStreamToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 17/04/2021
 */
public class ResourceStreamerTest
{
    @Test
    void test_fileExists()
    {
        var flow = Flow
            .from(new ResourceStreamer(), "simplelogger.properties")
            .pipe(new InputStreamToString())
            .collect()
        ;

        Assertions.assertDoesNotThrow(() -> {
            var output = Flow.runAsPipeline(flow);

            Assertions.assertTrue(output.get(flow).contains("org.slf4j.simpleLogger.defaultLogLevel"));
        });
    }

    @Test
    void test_fileDoesNotExist()
    {
        var flow = Flow
            .from(new ResourceStreamer(), "complicatedlogger.properties")
            .pipe(new InputStreamToString())
            .collect()
        ;

        Assertions.assertThrows(RunException.class, () -> Flow.runAsPipeline(flow));
    }
}
