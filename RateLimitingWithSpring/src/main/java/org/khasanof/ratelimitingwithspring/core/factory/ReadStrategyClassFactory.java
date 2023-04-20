package org.khasanof.ratelimitingwithspring.core.factory;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.config.ApplicationProperties;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffReadStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
public class ReadStrategyClassFactory {

    private final ApplicationContext context;

    public LimitReadStrategy limitReadStrategy(String path) {
        return genericReadStrategy(path, ApplicationProperties.LIMIT, LimitReadStrategy.class);
    }

    public TariffReadStrategy tariffReadStrategy(String path) {
        return genericReadStrategy(path, ApplicationProperties.TARIFF, TariffReadStrategy.class);
    }

    private <T> T genericReadStrategy(String path, String extraName, Class<T> aClass) {
        Assert.notNull(path, "path param is null!");
        Assert.notNull(extraName, "extraName param is null!");
        Assert.notNull(aClass, "aClass param is null!");

        String extension = StringUtils.getFilenameExtension(path);
        assert extension != null;
        return switch (extension) {
            case "json", "yaml" -> context.getBean(extraName + StringUtils.capitalize(extension)
                            .concat(ApplicationProperties.READ_STRATEGY),
                    aClass);
            default -> throw new IllegalStateException("Invalid File Extension: " + extension);
        };
    }

}
