package com.noleme.flow.connect.tablesaw.dataframe;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.commons.transformer.filesystem.ResourceStreamer;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.iostream.TablePropertiesJSONStreamLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.resource.TablePropertiesResourceLoader;
import com.noleme.flow.connect.tablesaw.transformer.TablesawCSVParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.tablesaw.api.Table;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/05/01
 */
public class ConfigurationParsingTest
{
    private final TablePropertiesLoader<String> loader = new TablePropertiesResourceLoader(new TablePropertiesJSONStreamLoader());

    @Test
    void configurationParsingTest()
    {
        Assertions.assertDoesNotThrow(() -> {
            this.loader.load("com/noleme/flow/connect/tablesaw/test.json");
        });
    }

    @Test
    void transformerTest() throws TablePropertiesLoadingException, CompilationException, RunException
    {
        var flow = Flow
            .from(new ResourceStreamer(), "com/noleme/flow/connect/tablesaw/test.csv")
            .into(new TablesawCSVParser(this.loader.load("com/noleme/flow/connect/tablesaw/test.json")))
        ;

        var columnCount = flow.pipe(Table::columnCount).collect();
        var rowCount = flow.pipe(Table::rowCount).collect();
        var indexExists = flow.pipe(t -> t.column("index") != null).collect();

        var output = Flow.runAsPipeline(flow);

        Assertions.assertEquals(5, output.get(columnCount));
        Assertions.assertEquals(3, output.get(rowCount));
        Assertions.assertTrue(output.get(indexExists));
    }
}
