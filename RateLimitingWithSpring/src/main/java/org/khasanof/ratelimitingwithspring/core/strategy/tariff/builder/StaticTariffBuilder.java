package org.khasanof.ratelimitingwithspring.core.strategy.tariff.builder;

import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.khasanof.ratelimitingwithspring.core.domain.embeddable.LimitsEmbeddable;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 8:02 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.strategy.tariff.builder
 */
public abstract class StaticTariffBuilder {

    public static List<Tariff> buildTariffList(List<RSTariff> tariffs) {
        return tariffs.stream()
                .map(StaticTariffBuilder::buildTariff)
                .toList();
    }

    public static Tariff buildTariff(RSTariff tariff) {
        return Tariff.builder()
                .name(tariff.getName())
                .limitsEmbeddable(LimitsEmbeddable.builder()
                        .undiminishedCount(tariff.getRequestCount())
                        .requestCount(tariff.getRequestCount())
                        .requestType(tariff.getRequestType())
                        .timeCount(tariff.getTimeCount())
                        .timeType(tariff.getTimeType())
                        .build())
                .build();
    }

}
