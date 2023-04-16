package org.khasanof.ratelimitingwithspring.core;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.factory.ReadStrategyFactory;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.read.ReadLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.save.ApiESLStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 2:58 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
@Service
@RequiredArgsConstructor
public class CommonLimitsAdapter {

    @Value("${api.limit.path}")
    private String PATH;
    private List<ReadLimit> limits = new ArrayList<>();
    private final ReadStrategyFactory strategyFactory;
    private final ApiESLStrategy eslStrategy;

    public List<ReadLimit> getLimits() {
        if (limits.isEmpty()) {
            List<ReadLimit> list = getLimitedPaths();
            setLimits(list);
            saveLimits(list);
        }
        return limits;
    }

    public List<String> getLimitApis() {
        return getLimits().stream()
                .map(ReadLimit::getUrl).toList();
    }

    private List<ReadLimit> getLimitedPaths() {
        try {
            AbstractReadStrategy readStrategy = strategyFactory.abstractReadStrategy(PATH);
            return readStrategy.readLimitList(PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveLimits(List<ReadLimit> limits) {
        eslStrategy.saveAll(limits);
    }

    private void setLimits(List<ReadLimit> limits) {
        this.limits = limits;
    }
}
