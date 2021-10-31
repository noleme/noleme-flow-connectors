package com.noleme.flow.connect.avro.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.avro.AvroHelper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.IOException;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 */
public class JsonDeserializer<T extends SpecificRecordBase> implements Transformer<String, T>
{
    private final Schema schema;

    public JsonDeserializer(Class<T> targetType)
    {
        this.schema = AvroHelper.getSchema(targetType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T transform(String json) throws TransformationException
    {
        if (json == null)
            return null;

        try {
            DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(this.schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(this.schema, json);
            return (T) datumReader.read(null, decoder);
        }
        catch (IOException e) {
            throw new TransformationException("Unable to deserialize incoming JSON", e);
        }
    }
}
