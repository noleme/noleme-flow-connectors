package com.noleme.flow.connect.avro.transformer;

import com.noleme.flow.actor.transformer.TransformationException;
import com.noleme.flow.actor.transformer.Transformer;
import com.noleme.flow.connect.avro.AvroHelper;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 */
public class AvroSerializer<T extends SpecificRecordBase> implements Transformer<T, Byte[]>
{
    @Override
    public Byte[] transform(T payload) throws TransformationException
    {
        if (payload == null)
            return null;

        try {
            var outputStream = new ByteArrayOutputStream();
            BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            DatumWriter<GenericRecord> datumWriter = new SpecificDatumWriter<>(payload.getSchema());
            datumWriter.write(payload, binaryEncoder);
            binaryEncoder.flush();
            outputStream.close();

            return AvroHelper.toBoxed(outputStream.toByteArray());
        }
        catch (IOException e) {
            throw new TransformationException("Unable to serialize payload of type "+payload.getClass().getName(), e);
        }
    }
}
