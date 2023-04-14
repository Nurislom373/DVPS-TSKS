package org.khasanof.ratelimitingwithspring.core.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties;
import org.khasanof.ratelimitingwithspring.core.strategy.read.ReadLimit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 11:07 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.json
 */
@Slf4j
@Service(JsonReadStrategy.SERVICE_NAME)
public class JsonReadStrategy extends AbstractReadStrategy {

    public static final String SERVICE_NAME = "json" + ApplicationProperties.READ_STRATEGY;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<ReadLimit> readLimitList(String path) throws IOException {
        log.info("Json Started");
        return objectMapper.readValue(getInputStream(path), new TypeReference<>() {
        });
    }

    private InputStream getInputStream(String path) {
        return getClass().getResourceAsStream(path);
    }

}
