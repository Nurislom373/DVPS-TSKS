package org.khasanof.ratelimitingwithspring.cache.ehcache;

import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.khasanof.ratelimitingwithspring.cache.MergeValue;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/24/2023
 * <br/>
 * Time: 10:41 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.cache
 */
public class MergeValueSerializer implements Serializer<MergeValue> {

    public MergeValueSerializer(ClassLoader classLoader) {
    }

    /**
     * Transforms the given instance into its serial form.
     *
     * @param object the instance to serialize
     * @return the binary representation of the serial form
     * @throws SerializerException if serialization fails
     */
    @Override
    public ByteBuffer serialize(MergeValue object) throws SerializerException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return ByteBuffer.wrap(outputStream.toByteArray());
        } catch (IOException e) {
            throw new SerializerException("Unable to serialize object", e);
        }
    }

    /**
     * Reconstructs an instance from the given serial form.
     *
     * @param binary the binary representation of the serial form
     * @return the de-serialized instance
     * @throws SerializerException    if reading the byte buffer fails
     * @throws ClassNotFoundException if the type to de-serialize to cannot be found
     */
    @Override
    public MergeValue read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(binary.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            return (MergeValue) objectInputStream.readObject();
        } catch (IOException e) {
            throw new SerializerException("Unable to deserialize object", e);
        }
    }

    /**
     * Checks if the given instance and serial form {@link Object#equals(Object) represent} the same instance.
     *
     * @param object the instance to check
     * @param binary the serial form to check
     * @return {@code true} if both parameters represent equal instances, {@code false} otherwise
     * @throws SerializerException    if reading the byte buffer fails
     * @throws ClassNotFoundException if the type to de-serialize to cannot be found
     */
    @Override
    public boolean equals(MergeValue object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
        return object != null;
    }
}
