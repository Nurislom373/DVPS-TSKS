package org.khasanof.ratelimitingwithspring.core.common.load.genericLoad;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.factory.ReadStrategyClassFactory;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.LimitReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 6:34 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.load
 */
@Slf4j
@Service
public class RSLimitLoadPostConstruct extends AbstractGenericLoadIPostConstruct<RSLimit> {

    public RSLimitLoadPostConstruct(ReadStrategyClassFactory classFactory) {
        super(classFactory);
    }

    @Override
    public void afterPropertiesSet(String path) {
        checkPathAndSetFalse(path);
        try {
            LimitReadStrategy limitReadStrategy = classFactory.limitReadStrategy(path);
            setList(limitReadStrategy.read(path));
            setPresent(true);
        } catch (IOException e) {
            setList(new ArrayList<>());
            setPresent(false);
            throw new RuntimeException(e);
        }
    }
}
