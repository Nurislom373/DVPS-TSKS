package org.khasanof.ratelimitingwithspring.core;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.factory.ReadStrategyFactory;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.read.ReadLimit;
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

    private List<ReadLimit> getLimitedPaths() {
        try {
            AbstractReadStrategy readStrategy = strategyFactory.abstractReadStrategy(PATH);
            return readStrategy.readLimitList(PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReadLimit> getLimits() {
        if (limits.isEmpty()) {
            setLimits(getLimitedPaths());
        }
        return limits;
    }

    public void setLimits(List<ReadLimit> limits) {
        this.limits = limits;
    }
}
