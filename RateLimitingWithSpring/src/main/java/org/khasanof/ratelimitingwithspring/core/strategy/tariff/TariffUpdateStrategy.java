package org.khasanof.ratelimitingwithspring.core.strategy.tariff;

import org.khasanof.ratelimitingwithspring.core.repository.TariffRepository;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractUpdateStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 7:58 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff
 */
public abstract class TariffUpdateStrategy extends AbstractUpdateStrategy<RSTariff, TariffRepository> {

    public TariffUpdateStrategy(TariffRepository repository) {
        super(repository);
    }
}
