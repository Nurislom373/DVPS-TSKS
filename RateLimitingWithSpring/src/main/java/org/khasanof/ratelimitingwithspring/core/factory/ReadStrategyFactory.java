package org.khasanof.ratelimitingwithspring.core.factory;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractReadStrategy;
import org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 3:06 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
@Service
@RequiredArgsConstructor
public class ReadStrategyFactory {

    private final ApplicationContext context;

    public AbstractReadStrategy abstractReadStrategy(String path) {
        if (Objects.isNull(path)) {
            throw new RuntimeException("Invalid Param!");
        }
        String extension = StringUtils.getFilenameExtension(path);
        assert extension != null;
        return switch (extension) {
            case "json", "yaml" -> context.getBean(extension.concat(ApplicationProperties.READ_STRATEGY),
                    AbstractReadStrategy.class);
            default -> throw new IllegalStateException("Invalid File Extension: " + extension);
        };
    }

}
