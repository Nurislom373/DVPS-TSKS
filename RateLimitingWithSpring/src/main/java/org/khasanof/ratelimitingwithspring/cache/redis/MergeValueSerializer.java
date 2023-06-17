package org.khasanof.ratelimitingwithspring.cache.redis;

import org.khasanof.ratelimitingwithspring.cache.MergeValue;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/24/2023
 * <br/>
 * Time: 11:22 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.config
 */
public class MergeValueSerializer implements RedisSerializer<MergeValue> {

    private final Converter<Object, byte[]> serializer = new SerializingConverter();
    private final Converter<byte[], Object> deserializer = new DeserializingConverter();

    @Override
    public byte[] serialize(MergeValue mergeValue) throws SerializationException {
        return serializer.convert(mergeValue);
    }

    @Override
    public MergeValue deserialize(byte[] bytes) throws SerializationException {
        return (MergeValue) deserializer.convert(bytes);
    }
}
