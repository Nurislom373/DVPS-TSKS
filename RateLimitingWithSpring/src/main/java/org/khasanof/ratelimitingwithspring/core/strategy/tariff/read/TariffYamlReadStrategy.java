package org.khasanof.ratelimitingwithspring.core.strategy.tariff.read;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties.READ_STRATEGY;
import static org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties.YAML;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 4:57 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff.read
 */
@Slf4j
@Service
public class TariffYamlReadStrategy extends TariffReadStrategy {

    public static final String SERVICE_NAME = "tariff" + YAML + READ_STRATEGY;
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    @Override
    public List<RSTariff> read(String path) throws IOException {
        log.info("Read Method Started Work in {}", SERVICE_NAME);
        return objectMapper.readValue(getResource(path), new TypeReference<>() {
        });
    }

    private InputStream getResource(String path) {
        log.info("Get Resource With {}. This url - {}", SERVICE_NAME, path);
        return getClass().getResourceAsStream(path);
    }
}
