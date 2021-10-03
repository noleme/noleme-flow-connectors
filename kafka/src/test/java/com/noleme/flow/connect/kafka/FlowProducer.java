package com.noleme.flow.connect.kafka;

import com.noleme.flow.compiler.CompilationException;
import com.noleme.flow.compiler.RunException;
import com.noleme.flow.connect.kafka.flow.KafkaFlow;
import com.noleme.flow.connect.kafka.vault.KafkaModule;
import com.noleme.vault.Vault;
import com.noleme.vault.exception.VaultException;
import com.noleme.vault.factory.VaultFactory;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class FlowProducer
{
    public static void main(String[] args) throws VaultException, CompilationException, RunException
    {
        VaultFactory.defaultParser.register(new KafkaModule());

        try (var vault = Vault.with("vault/noleme-flow-connect-kafka/kafka.yml"))
        {
            vault.instance(KafkaFlow.class).run();
        }
    }
}
