package com.noleme.flow.connect.tablesaw.transformer;

import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.TableProperties;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoadingException;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.file.TablePropertiesFileLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.iostream.TablePropertiesJSONStreamLoader;
import com.noleme.flow.connect.tablesaw.dataframe.processor.CompositeProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.TableProcessorException;
import com.noleme.flow.connect.tablesaw.dataframe.processor.column.AddColumnRowIndexPropertiesProcessor;
import com.noleme.flow.connect.tablesaw.dataframe.processor.column.RenameColumnPropertiesProcessor;
import tech.tablesaw.api.Table;

import java.io.InputStream;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 18/04/2021
 */
public abstract class TablesawParser implements Transformer<InputStream, Table>
{
    protected final TableProperties properties;
    protected final CompositeProcessor preprocessor;

    protected TablesawParser(TableProperties properties)
    {
        this.properties = properties;
        this.preprocessor = new CompositeProcessor()
            .addProcessor(new RenameColumnPropertiesProcessor(properties))
            .addProcessor(new AddColumnRowIndexPropertiesProcessor(properties))
        ;
    }

    /**
     *
     * @param confPath
     * @throws TablePropertiesLoadingException
     */
    public TablesawParser(String confPath) throws TablePropertiesLoadingException
    {
        this(new TablePropertiesFileLoader(new TablePropertiesJSONStreamLoader()).load(confPath));
    }

    /**
     *
     * @param table
     * @return
     * @throws TableProcessorException
     */
    protected Table postBuild(Table table) throws TableProcessorException
    {
        if (this.properties.getName() != null)
            table.setName(this.properties.getName());

        return this.preprocessor.process(table);
    }
}
