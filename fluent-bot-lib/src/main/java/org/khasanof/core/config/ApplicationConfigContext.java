package org.khasanof.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.Asserts;
import org.khasanof.core.utils.BaseUtils;

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

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
       return (T) classObjectMap.get(clazz);
    }

    public void addAll(Map<Class<?>, Object> classObjectMap) {
        BaseUtils.checkArgsIsNonNull(classObjectMap);
        this.classObjectMap.putAll(classObjectMap);
    }

    public void add(Class<?> key, Object value) {
        BaseUtils.checkArgsIsNonNull(key, value);
        this.classObjectMap.put(key, value);
    }

    public static ApplicationConfigContext getConfigInstance() {
        return APPLICATION_CONFIG_CONTEXT;
    }

}
