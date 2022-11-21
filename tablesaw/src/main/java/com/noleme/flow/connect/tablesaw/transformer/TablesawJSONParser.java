package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.ColumnProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.RuntimeIOException;
import tech.tablesaw.io.Source;
import tech.tablesaw.io.json.JsonReadOptions;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 18/04/2021
 */
public class TablesawJSONParser extends TablesawParser
{
    private static final Logger logger = LoggerFactory.getLogger(TablesawJSONParser.class);

    public TablesawJSONParser()
    {
        super(new TableProperties());
    }

    public TablesawJSONParser(TableProperties properties)
    {
        super(properties);
    }

    public TablesawJSONParser(String confPath) throws TablePropertiesLoadingException
    {
        super(confPath);
    }

    @Override
    public Table transform(InputStream input) throws TransformationException
    {
        try {
            JsonReadOptions options = this.prepareOptions(input);

            logger.info("Extracting JSON data into dataframe...");

            Table table = Table.read().usingOptions(options);
            table = this.applyMapping(table);
            table = this.postBuild(table);

            logger.info("Extracted {} lines into{} dataframe.", table.rowCount(), (!table.name().isEmpty() ? " \"" + table.name() + "\"" : ""));

            return table;
        }
        catch (RuntimeIOException | IndexOutOfBoundsException | TableProcessorException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }

    /**
     *
     * @param input
     * @return
     */
    private JsonReadOptions prepareOptions(InputStream input)
    {
        JsonReadOptions.Builder builder = (JsonReadOptions.Builder) JsonReadOptions.builder(new Source(input, this.properties.getCharset()))
            .header(this.properties.hasHeader())
            .maxCharsPerColumn(this.properties.getMaxCharsPerColumn())
        ;

        if (this.properties.getSampleSize() >= 0)
            logger.warn("A TableProperties sample_size was specified but tablesaw-json cannot handle it");
        if (!this.properties.getMapping().isEmpty())
            logger.warn("A TableProperties mapping was specified but tablesaw-json cannot handle it, column mapping will be emulated afterwards");

        return builder.build();
    }

    /**
     *
     * @param table
     * @return
     */
    private Table applyMapping(Table table)
    {
        if (this.properties.getMapping().isEmpty())
            return table;

        List<ColumnProperties> props = this.properties.getMapping().stream()
            .sorted(Comparator.comparingInt(ColumnProperties::getSourceIndex))
            .collect(Collectors.toList())
        ;

        for (ColumnProperties p : props)
        {
            if (p.getType() == ColumnType.SKIP)
            {
                if (table.column(p.getSourceIndex()) != null)
                    table.removeColumns(p.getSourceIndex());
            }
        }

        return table;
    }
}
