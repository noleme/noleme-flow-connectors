package com.noleme.flow.connect.tablesaw.vault;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.TablePropertiesLoader;
import com.noleme.flow.connect.tablesaw.dataframe.configuration.loader.json.TablePropertiesNodeLoader;
import com.noleme.vault.container.register.Definitions;
import com.noleme.vault.container.definition.ServiceProvider;
import com.noleme.vault.parser.module.VaultModule;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/11/24
 */
public class MappingModule implements VaultModule
{
    private final TablePropertiesLoader<ObjectNode> loader = new TablePropertiesNodeLoader();

    @Override
    public String identifier()
    {
        return "mapping";
    }

    @Override
    public void process(ObjectNode node, Definitions definitions)
    {
        node.fields().forEachRemaining(entry -> {
            ObjectNode json = (ObjectNode) entry.getValue();
            String serviceIdentifier = json.get("id").asText();

            var def = new ServiceProvider(
                serviceIdentifier,
                TablePropertiesLoader.class.getName(),
                "load"
            ).setMethodArgs(new Object[]{
                this.loader,
                json
            });

            definitions.services().set(serviceIdentifier, def);
        });
    }
}
