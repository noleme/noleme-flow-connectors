package com.noleme.flow.connect.kafka.vault;

import com.noleme.flow.connect.kafka.config.KafkaConfig;
import com.noleme.vault.container.definition.ServiceInstantiation;
import com.noleme.vault.container.definition.ServiceValue;
import com.noleme.vault.container.register.Definitions;
import com.noleme.vault.exception.VaultParserException;
import com.noleme.vault.parser.module.GenericModule;

/**
 * @author Pierre LECERF (pierre@noleme.com)
 * Created on 22/09/2021
 */
public class KafkaModule extends GenericModule<KafkaConfig>
{
    private static final String CONFIG_ID = "kafka_config";
    private static final String TOPIC_CREATOR_ID = "kafka_topic_creator";

    public KafkaModule()
    {
        super("kafka", KafkaConfig.class, KafkaModule::processConfig);
    }

    private static void processConfig(KafkaConfig cfg, Definitions defs) throws VaultParserException
    {
        if (cfg.topics != null)
            addTopicCreator(defs);

        defs.services().set(CONFIG_ID, new ServiceValue<>(CONFIG_ID, cfg));
    }

    private static void addTopicCreator(Definitions defs)
    {
        final Object[] args = { defs.services().reference(CONFIG_ID) };
        defs.services().set(
            TOPIC_CREATOR_ID,
            new ServiceInstantiation(
                TOPIC_CREATOR_ID,
                KafkaTopicCreator.class.getName(),
                args
            )
        );
    }
}
