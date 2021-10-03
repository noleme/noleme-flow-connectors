package com.noleme.flow.connect.biteydf.transformer;

import com.noleme.commons.container.Pair;
import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.connect.biteydf.vault.config.ColumnProperties;
import com.noleme.flow.connect.biteydf.vault.config.TableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.bitey.dataframe.ColumnType;
import tech.bitey.dataframe.DataFrame;
import tech.bitey.dataframe.DataFrameFactory;
import tech.bitey.dataframe.ReadCsvConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/10/2021
 */
public class BiteyDataframeCSVParser extends BiteyDataframeParser
{
    private static final Logger logger = LoggerFactory.getLogger(BiteyDataframeCSVParser.class);

    public BiteyDataframeCSVParser(TableProperties properties)
    {
        super(properties);
    }

    @Override
    public DataFrame transform(InputStream input) throws TransformationException
    {
        try {
            ReadCsvConfig config = this.prepareOptions();

            logger.info("Extracting CSV data into dataframe...");

            DataFrame table = DataFrameFactory.readCsvFrom(input, config);
            table = this.postBuild(table);

            logger.info("Extracted {} lines into dataframe.", table.size());

            return table;
        }
        catch (IOException e) {
            throw new TransformationException(e.getMessage(), e);
        }
    }

    private ReadCsvConfig prepareOptions()
    {
        ReadCsvConfig.Builder builder = ReadCsvConfig.builder()
            .delim(this.properties.getSeparator())
        ;

        if (!this.properties.getColumns().isEmpty())
        {
            var autocompleted = computeColumnTypes(this.properties);
            builder.columnTypes(autocompleted.first);
            if (!this.properties.hasHeader())
                builder.columnNames(autocompleted.second);
        }

        return builder.build();
    }

    private static Pair<List<ColumnType<?>>, List<String>> computeColumnTypes(TableProperties properties)
    {
        List<ColumnProperties> declaredTypes = properties.getColumns()
            .stream()
            .sorted(Comparator.comparingInt(ColumnProperties::getIndex))
            .collect(Collectors.toList())
        ;

        List<ColumnType<?>> autocompleted = new ArrayList<>();
        List<String> names = new ArrayList<>();

        int i = 0;
        for (ColumnProperties cp : declaredTypes)
        {
            for (; i < cp.getIndex(); ++i)
            {
                autocompleted.add(ColumnType.STRING);
                names.add("");
            }

            autocompleted.add(cp.getType());
            names.add(cp.getName());

            i = cp.getIndex() + 1;
        }
        for (; i < properties.getColumnCount(); ++i)
        {
            autocompleted.add(ColumnType.STRING);
            names.add("");
        }

        return new Pair<>(autocompleted, names);
    }

    @Override
    protected DataFrame postBuild(DataFrame table)
    {
        if (table.columnCount() > this.properties.getColumns().size())
        {
            Set<Integer> requested = this.properties.getColumns().stream()
                .map(ColumnProperties::getIndex)
                .collect(Collectors.toSet())
            ;

            List<Integer> droppedList = IntStream
                .rangeClosed(0, table.columnCount() - 1)
                .boxed()
                .filter(idx -> !requested.contains(idx))
                .collect(Collectors.toList())
            ;

            int[] dropped = new int[droppedList.size()];
            for (int i = 0 ; i < droppedList.size() ; ++i)
                dropped[i] = droppedList.get(i);

            table = table.dropColumns(dropped);
        }

        return table;
    }
}
