package org.khasanof.ratelimitingwithspring.core.strategy.limit.read;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties.JSON;
import static org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties.READ_STRATEGY;

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
@Service(LimitJsonReadStrategy.SERVICE_NAME)
public class LimitJsonReadStrategy extends LimitReadStrategy {

    public static final String SERVICE_NAME = "limit" + JSON + READ_STRATEGY;
    private final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

    @Override
    public List<RSLimit> read(String path) throws IOException {
        log.info("Read Method Started Work in {}", SERVICE_NAME);
        return objectMapper.readValue(getResource(path), new TypeReference<>() {
        });
    }

    private InputStream getResource(String path) {
        log.info("Get Resource With {}. This url - {}", SERVICE_NAME, path);
        return getClass().getResourceAsStream(path);
    }

}
