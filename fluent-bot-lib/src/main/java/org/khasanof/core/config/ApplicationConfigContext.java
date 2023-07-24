package org.khasanof.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.Asserts;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The main purpose of this class is to store subclasses of the Config interface
 * and to return an instance of this requested class.
 *
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 22.07.2023 21:43
 */
public class ApplicationConfigContext {

    // TODO will be written sometime
    private static final ApplicationConfigContext APPLICATION_CONFIG_CONTEXT = new ApplicationConfigContext();
    private final Map<Class<?>, Object> classObjectMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T getInstance(Class<T> clazz) {
       return objectMapper.convertValue(classObjectMap.get(clazz), clazz);
    }

    public void addAll(Map<Class<?>, Object> classObjectMap) {
        Asserts.check(Objects.nonNull(classObjectMap), "classObjectMap param must be not null!");
        this.classObjectMap.putAll(classObjectMap);
    }

}
