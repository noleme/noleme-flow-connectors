package com.noleme.flow.connect.avro;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecordBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author Pierre Lecerf (pierre@noleme.com)
 */
public final class AvroHelper
{
    private AvroHelper() {}

    /**
     *
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T extends SpecificRecordBase> Schema getSchema(Class<T> targetType)
    {
        try {
            Constructor<T> ctor = targetType.getConstructor();
            T record = ctor.newInstance();
            return record.getSchema();
        }
        /* Given we work with generated avro classes these should never happen */
        catch (NoSuchMethodException e) {
            throw new RuntimeException("The provided target type is expected to have a no-arg constructor.", e);
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("An unexpected error occurred while attempting to create an instance of the provided target type.", e);
        }
    }

    public static byte[] toPrimitive(Byte[] bytes)
    {
        byte[] primitives = new byte[bytes.length];
        for (int i = 0 ; i < bytes.length ; ++i)
            primitives[i] = bytes[i];

        return primitives;
    }

    public static Byte[] toBoxed(byte[] bytes)
    {
        Byte[] boxed = new Byte[bytes.length];
        Arrays.setAll(boxed, n -> bytes[n]);
        return boxed;
    }
}
