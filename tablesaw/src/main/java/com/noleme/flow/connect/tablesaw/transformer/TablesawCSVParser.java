package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.ColumnProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.file.TablePropertiesFileLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.iostream.TablePropertiesJSONStreamLoader;
import com.noleme.flow.connect.tablesaw.dataframe.processor.CompositeProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import com.noleme.flow.connect.tablesaw.dataframe.processor.column.AddColumnRowIndexPropertiesProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.column.RenameColumnPropertiesProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.Source;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/02/27
 */
public class TablesawCSVParser implements Transformer<InputStream, Table>
{
    private final TableProperties properties;
    private final TableProcessor preprocessor;

    private static final Logger logger = LoggerFactory.getLogger(TablesawCSVParser.class);

    public TablesawCSVParser()
    {
        this(new TableProperties());
    }

    /**
     *
     * @param properties
     */
    public TablesawCSVParser(TableProperties properties)
    {
        this.properties = properties;
        this.preprocessor = new CompositeProcessor()
            .addProcessor(new RenameColumnPropertiesProcessor(properties))
            .addProcessor(new AddColumnRowIndexPropertiesProcessor(properties));
    }

    /**
     *
     * @param confPath
     * @throws TablePropertiesLoadingException
     */
    public TablesawCSVParser(String confPath) throws TablePropertiesLoadingException
    {
        this(new TablePropertiesFileLoader(new TablePropertiesJSONStreamLoader()).load(confPath));
    }

    @Override
    public Table transform(InputStream input) throws TransformationException
    {
        try {
            CsvReadOptions options = this.prepareOptions(input);

            logger.info("Extracting CSV data into dataframe...");

            Table table = Table.read().csv(options);
            table = this.postBuild(table);

            logger.info("Extracted {} lines into{} dataframe.", table.rowCount(), (!table.name().isEmpty() ? " \"" + table.name() + "\"" : ""));

            return table;
        }
        catch (IOException | IndexOutOfBoundsException | TableProcessorException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }

    /**
     *
     * @param input
     * @return
     */
    private CsvReadOptions prepareOptions(InputStream input)
    {
        CsvReadOptions.Builder builder = CsvReadOptions.builder(new Source(input, this.properties.getCharset()))
            .separator(this.properties.getSeparator())
            .quoteChar(this.properties.getQuoteChar())
            .header(this.properties.hasHeader())
            .maxCharsPerColumn(this.properties.getMaxCharsPerColumn());

        if (this.properties.getSampleSize() >= 0)
            builder.sampleSize(this.properties.getSampleSize());
        if (!this.properties.getMapping().isEmpty())
            builder.columnTypes(computeColumnTypes(this.properties));

        return builder.build();
    }

    /**
     *
     * @param table
     * @return
     * @throws TableProcessorException
     */
    private Table postBuild(Table table) throws TableProcessorException
    {
        if (this.properties.getName() != null)
            table.setName(this.properties.getName());

        return this.preprocessor.process(table);
    }

    /**
     * Computes a complete array of ColumnType that we can give to the Tablesaw builder in order to detect column types.
     * The idea here is that we SKIP everything that isn't explicitly declared in the mapping configuration, so we need to fill the blank areas with SKIP ColumnTypes.
     *
     * @param properties
     * @return
     */
    private static ColumnType[] computeColumnTypes(TableProperties properties)
    {
        List<ColumnProperties> declaredTypes = properties.getMapping()
            .stream()
            .sorted(Comparator.comparingInt(ColumnProperties::getSourceIndex))
            .collect(Collectors.toList())
        ;

        ArrayList<ColumnType> autocompleted = new ArrayList<>();

        int i = 0;
        for (ColumnProperties cp : declaredTypes) {
            for (; i < cp.getSourceIndex(); ++i)
                autocompleted.add(ColumnType.SKIP);

            autocompleted.add(cp.getType());

            i = cp.getSourceIndex() + 1;
        }
        for (; i < properties.getColumnCount(); ++i)
            autocompleted.add(ColumnType.SKIP);

        return autocompleted.toArray(new ColumnType[0]);
    }
}
