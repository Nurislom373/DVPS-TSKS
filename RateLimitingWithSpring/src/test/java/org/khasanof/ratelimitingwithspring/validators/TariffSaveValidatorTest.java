package org.khasanof.ratelimitingwithspring.validators;

import org.junit.jupiter.api.Test;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.tariff.TariffSaveValidator;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 11.05.2023
 * <br/>
 * Time: 10:06
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.validators
 */
public class TariffSaveValidatorTest {

    private final TariffSaveValidator validator = new TariffSaveValidator();

    @Test
    void successfullyValidationTest() {
        List<RSTariff> rsTariffs = simpleThree();
        ValidatorResult validatorResult = validator.validatorRSTariff(rsTariffs);

        assertAll(
                () -> {
                    assertTrue(validatorResult.isSuccess());
                    assertTrue(Objects.isNull(validatorResult.getData()));
                    assertEquals(ValidatorResult.DEFAULT_MESSAGE, validatorResult.getMessage());
                }
        );
    }

    private List<RSTariff> simpleThree() {
        return List.of(
          new RSTariff("FREE", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
          new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
          new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
        );
    }




}
