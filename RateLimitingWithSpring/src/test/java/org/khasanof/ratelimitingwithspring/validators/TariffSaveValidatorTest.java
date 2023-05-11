package org.khasanof.ratelimitingwithspring.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;
import org.khasanof.ratelimitingwithspring.core.strategy.tariff.classes.RSTariff;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.tariff.TariffSaveValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("getNullAndEmptyArguments")
    void nullAndEmptyListTest(List<RSTariff> list) {
        assertAll(
                () -> assertFalse(validator.validatorRSTariff(list).isSuccess())
        );
    }

    static Stream<List<RSTariff>> getNullAndEmptyArguments() {
        return Stream.of(new ArrayList<>(), null);
    }

    @Test
    void duplicateTariffTest() {
        assertAll(
                () -> {
                    InvalidValidationException validationException = assertThrows(InvalidValidationException.class,
                            () -> validator.validatorRSTariff(List.of(
                                    new RSTariff("PRO", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                                    new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                                    new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                            )));
                    assertEquals(validationException.getMessage(), "Duplicate tariff!");
                }
        );
    }

    @ParameterizedTest
    @MethodSource("unsuccessfulValidationSource")
    void unsuccessfulValidationTest(List<RSTariff> list, String message) {
        assertAll(
                () -> {
                    InvalidValidationException validationException = assertThrows(InvalidValidationException.class,
                            () -> validator.validatorRSTariff(list));
                    assertEquals(validationException.getMessage(), message);
                }
        );
    }

    static Stream<Arguments> unsuccessfulValidationSource() {
        return Stream.of(
                Arguments.arguments(List.of(
                        new RSTariff("fREE", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => name must contain only letters and upperCase!"),
                Arguments.arguments(List.of(
                        new RSTariff(" ", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => name must contain only letters and upperCase!"),
                Arguments.arguments(List.of(
                        new RSTariff(null, RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => name must contain only letters and upperCase!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", null, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => requestType must not be null!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", RequestType.LIMIT, -100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => requestCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 0L, TimeType.DAY, 1L)
                ), "RSTariff field => requestCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", RequestType.LIMIT, 100L, null, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => timeType must not be null!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", RequestType.LIMIT, 100L, TimeType.DAY, -1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
                ), "RSTariff field => timeCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, -1L)
                ), "RSTariff field => timeCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSTariff("FREE", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                        new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                        new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 0L)
                ), "RSTariff field => timeCount is less than one!")
        );
    }

    private static List<RSTariff> simpleThree() {
        return List.of(
                new RSTariff("FREE", RequestType.LIMIT, 100L, TimeType.DAY, 1L),
                new RSTariff("BASIC", RequestType.LIMIT, 1000L, TimeType.DAY, 1L),
                new RSTariff("PRO", RequestType.LIMIT, 10000L, TimeType.DAY, 1L)
        );
    }


}
