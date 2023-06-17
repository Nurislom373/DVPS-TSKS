package org.khasanof.ratelimitingwithspring.core.utils.valueBuilder;

import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;

import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.05.2023
 * <br/>
 * Time: 16:15
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.utils.valueBuilder
 */
public interface ValueBuilder<R> {

    Map<String, Map<PTA, R>> pricingAPIListToInnerMap(List<PricingApi> list);

    Map<String, Map<PTA, R>> pricingTariffListToInnerMap(List<PricingTariff> list);

    Map<PTA, R> pricingAPIListToMap(List<PricingApi> list);

    Map<PTA, R> pricingTariffListToMap(List<PricingTariff> list);

    Map.Entry<PTA, R> convertEntityToEntry(PricingApi pricingApi);

    Map.Entry<PTA, R> convertEntityToEntry(PricingTariff pricingTariff);

}
