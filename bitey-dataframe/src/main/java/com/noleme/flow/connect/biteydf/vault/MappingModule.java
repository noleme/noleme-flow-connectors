package com.noleme.flow.connect.biteydf.vault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.noleme.flow.connect.biteydf.vault.config.MappingConfig;
import com.noleme.json.Json;
import com.noleme.vault.container.definition.ServiceValue;
import com.noleme.vault.container.register.Definitions;
import com.noleme.vault.parser.module.VaultModule;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/10/2021
 */
public class MappingModule implements VaultModule
{
    private static final ObjectMapper mapper = Json.newDefaultMapper();

    @Override
    public String identifier()
    {
        return "bitey_dataframe";
    }

    @Override
    public void process(ObjectNode node, Definitions definitions)
    {
        node.fieldNames().forEachRemaining(field -> {
            MappingConfig config = Json.fromJson(node.get(field), MappingConfig.class);

            definitions.services().set(config.id, new ServiceValue<>(config.id, config.properties));
        });
    }
}
