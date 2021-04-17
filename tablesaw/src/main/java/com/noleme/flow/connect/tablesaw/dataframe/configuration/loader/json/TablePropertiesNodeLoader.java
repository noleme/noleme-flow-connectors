package com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.connect.tablesaw.dataframe.column.ColumnTypes;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.ColumnProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import tech.tablesaw.api.ColumnType;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.noleme.commons.function.RethrowPredicate.rethrower;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/03/01
 */
public class TablePropertiesNodeLoader implements TablePropertiesLoader<ObjectNode>
{
    @Override
    public TableProperties load(ObjectNode json) throws TablePropertiesLoadingException
    {
        checkRequired(json, "column_count", "columns");

        var properties = new TableProperties()
            .setColumnCount(json.get("column_count").asInt())
            .setMapping(this.computeMapping(json.get("columns")))
        ;

        if (json.has("name"))
            properties.setName(json.get("name").asText());
        if (json.has("separator"))
            properties.setSeparator(json.get("separator").asText().charAt(0));
        if (json.has("quote_char"))
            properties.setQuoteChar(json.get("quote_char").asText().charAt(0));
        if (json.has("max_chars_per_column"))
            properties.setMaxCharsPerColumn(json.get("max_chars_per_column").asInt());
        if (json.has("charset"))
            properties.setCharset(Charset.forName(json.get("charset").asText()));
        if (json.has("has_header"))
            properties.setHasHeader(json.get("has_header").asBoolean());
        if (json.has("sample_size"))
            properties.setSampleSize(json.get("sample_size").asInt());
        if (json.has("add_row_index"))
            properties.setAddRowIndex(json.get("add_row_index").asBoolean());

        return properties;
    }

    /**
     *
     * @param json
     * @return
     * @throws TablePropertiesLoadingException
     */
    private List<ColumnProperties> computeMapping(JsonNode json) throws TablePropertiesLoadingException
    {
        List<ColumnProperties> properties = new ArrayList<>();
        Set<Integer> indexes = new HashSet<>();
        Set<String> colNames = new HashSet<>();

        int targetIndex = 0;

        for (JsonNode jsonMapping : prepareMappingJson(json))
        {
            ColumnType type = ColumnTypes.valueOf(jsonMapping.get("type").asText()).getType();

            ColumnProperties cp = new ColumnProperties(
                type,
                jsonMapping.has("name") ? jsonMapping.get("name").asText() : null,
                jsonMapping.get("index").asInt(),
                type != ColumnType.SKIP ? targetIndex : null
            );

            if (indexes.contains(cp.getSourceIndex()))
                throw new TablePropertiesLoadingException("The index " + cp.getSourceIndex() + " was specified more than once.");
            if (colNames.contains(cp.getName()))
                throw new TablePropertiesLoadingException("The name " + cp.getName() + " was specified more than once.");

            indexes.add(cp.getSourceIndex());
            colNames.add(cp.getName());

            properties.add(cp);

            if (cp.getType() != ColumnType.SKIP)
                targetIndex++;
        }

        return properties;
    }

    /**
     *
     * @param json
     * @return
     * @throws TablePropertiesLoadingException
     */
    private static Collection<JsonNode> prepareMappingJson(JsonNode json) throws TablePropertiesLoadingException
    {
        var iterator = json.elements();

        Iterable<JsonNode> iterable = () -> iterator;

        return StreamSupport
            .stream(iterable.spliterator(), false)
            .filter(rethrower(jsonMapping -> {
                checkRequired((ObjectNode) jsonMapping, "type", "index");
                return true;
            }))
            .sorted(Comparator.comparingInt(node -> node.get("index").asInt()))
            .collect(Collectors.toList())
        ;
    }

    /**
     *
     * @param json
     * @param fields
     * @throws TablePropertiesLoadingException
     */
    private static void checkRequired(ObjectNode json, String... fields) throws TablePropertiesLoadingException
    {
        for (String field : fields)
        {
            if (!json.has(field))
                throw new TablePropertiesLoadingException("The " + field + " property is required and could not be found in the mapping file.");
        }
    }
}
