package com.noleme.flow.connect.biteydf.vault;

import com.noleme.flow.Flow;
import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.biteydf.transformer.BiteyDataframeCSVParser;
import com.noleme.flow.connect.biteydf.vault.config.TableProperties;
import com.noleme.flow.connect.commons.transformer.filesystem.FlexibleStreamer;
import com.noleme.vault.Vault;
import com.noleme.vault.exception.VaultException;
import com.noleme.vault.factory.VaultFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 03/10/2021
 */
public class MappingModuleTest
{
    static {
        VaultFactory.defaultParser.register(new MappingModule());
    }

    @Test
    public void testMapping()
    {
        Assertions.assertDoesNotThrow(() -> {
            Vault.with("com/noleme/flow/connect/biteydf/schema.yml");
        });
    }

    @Test
    public void testCSVLoading() throws VaultException, CompilationException, RunException
    {
        try (var vault = Vault.with("com/noleme/flow/connect/biteydf/schema.yml"))
        {
            var props = vault.instance(TableProperties.class, "my_dataset.properties");

            var recipient = Flow
                .from(new FlexibleStreamer(), "com/noleme/flow/connect/biteydf/test-data.csv")
                .pipe(new BiteyDataframeCSVParser(props))
                .pipe(df -> df.longColumn("b").toDistinct().size())
                .collect()
            ;

            Assertions.assertEquals(3, Flow.runAsPipeline(recipient).get(recipient));
        }
    }
}
