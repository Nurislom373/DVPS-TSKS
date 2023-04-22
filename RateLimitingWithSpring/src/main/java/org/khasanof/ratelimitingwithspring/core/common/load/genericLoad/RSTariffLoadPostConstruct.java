package org.khasanof.ratelimitingwithspring.core.common.load.genericLoad;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.factory.ReadStrategyClassFactory;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.TariffReadStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 6:42 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.load.genericLoad
 */
@Slf4j
@Service
public class RSTariffLoadPostConstruct extends AbstractGenericLoadIPostConstruct<RSTariff> {

    protected RSTariffLoadPostConstruct(ReadStrategyClassFactory classFactory) {
        super(classFactory);
    }

    @Override
    public void afterPropertiesSet(String path) {
        checkPathAndSetFalse(path);
        try {
            if (list == null || list.isEmpty()) {
                TariffReadStrategy readStrategy = classFactory.tariffReadStrategy(path);
                setList(readStrategy.read(path));
                setPresent(true);
            }
        } catch (IOException e) {
            setList(new ArrayList<>());
            setPresent(false);
            throw new RuntimeException(e);
        }
    }
}
