package org.khasanof.ratelimitingwithspring.core.strategy.tariff;

import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.strategy.AbstractSaveStrategy;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.khasanof.ratelimitingwithspring.core.validator.tariff.TariffSaveValidator;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 5:58 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff
 */
@RequiredArgsConstructor
public abstract class TariffSaveStrategy extends AbstractSaveStrategy<RSTariff> {

    protected final TariffSaveValidator validator;

}
