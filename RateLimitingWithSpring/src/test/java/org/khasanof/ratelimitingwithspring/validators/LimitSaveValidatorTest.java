package org.khasanof.ratelimitingwithspring.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;
import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimitPlan;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.khasanof.ratelimitingwithspring.core.validator.limit.save.LimitSaveValidator;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 11.05.2023
 * <br/>
 * Time: 20:47
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.validators
 */
public class LimitSaveValidatorTest {

    private final LimitSaveValidator validator = new LimitSaveValidator();

    @Test
    void successfullyValidationTest() {
        List<RSLimit> rsLimits = simpleThree();
        ValidatorResult validatorResult = validator.validatorRSLimits(rsLimits);

        assertAll(
                () -> {
                    assertTrue(validatorResult.isSuccess());
                    assertTrue(Objects.isNull(validatorResult.getData()));
                    assertEquals(validatorResult.getMessage(), ValidatorResult.DEFAULT_MESSAGE);
                }
        );
    }

    @Test
    void duplicateRSLimitTest() {
        assertAll(
                () -> {
                    InvalidValidationException validationException = assertThrows(InvalidValidationException.class,
                            () -> validator.validatorRSLimits(duplicateThree()));
                    assertEquals(validationException.getMessage(), "Duplicate Limits!");
                }
        );
    }

    @ParameterizedTest
    @MethodSource("unsuccessfulValidationSource")
    void unsuccessfulValidationTest(List<RSLimit> list, String message) {
        assertAll(
                () -> {
                    InvalidValidationException validationException = assertThrows(InvalidValidationException.class,
                            () -> validator.validatorRSLimits(list));
                    assertEquals(validationException.getMessage(), message);
                }
        );
    }

    private static List<RSLimit> simpleThree() {
        return List.of(
                new RSLimit("/api/user", RequestMethod.POST, List.of(
                        new RSLimitPlan("FREE", RequestType.LIMIT, 100L, TimeType.WEEK, 1L),
                        new RSLimitPlan("BASIC", RequestType.LIMIT, 1000L, TimeType.WEEK, 1L)
                ), null),
                new RSLimit("/api/find", RequestMethod.GET, List.of(
                        new RSLimitPlan("FREE", RequestType.LIMIT, 100L, TimeType.WEEK, 1L),
                        new RSLimitPlan("BASIC", RequestType.LIMIT, 1000L, TimeType.WEEK, 1L)
                ), null),
                new RSLimit("/api/user", RequestMethod.GET, List.of(
                        new RSLimitPlan("FREE", RequestType.LIMIT, 100L, TimeType.WEEK, 1L),
                        new RSLimitPlan("BASIC", RequestType.LIMIT, 1000L, TimeType.WEEK, 1L)
                ), null)
        );
    }

    private static List<RSLimit> duplicateThree() {
        return List.of(
                new RSLimit("/api/user", RequestMethod.POST, List.of(
                        new RSLimitPlan("FREE", RequestType.LIMIT, 100L, TimeType.WEEK, 1L),
                        new RSLimitPlan("BASIC", RequestType.LIMIT, 1000L, TimeType.WEEK, 1L)
                ), null),
                new RSLimit("/api/find", RequestMethod.GET, List.of(
                        new RSLimitPlan("FREE", RequestType.LIMIT, 100L, TimeType.WEEK, 1L),
                        new RSLimitPlan("BASIC", RequestType.LIMIT, 1000L, TimeType.WEEK, 1L)
                ), null),
                new RSLimit("/api/user", RequestMethod.POST, List.of(
                        new RSLimitPlan("FREE", RequestType.LIMIT, 100L, TimeType.WEEK, 1L),
                        new RSLimitPlan("BASIC", RequestType.LIMIT, 1000L, TimeType.WEEK, 1L)
                ), null)
        );
    }

    static Stream<Arguments> unsuccessfulValidationSource() {
        return Stream.of(
                Arguments.arguments(List.of(
                       new RSLimit("/api/v1/jeck", RequestMethod.POST, List.of(new RSLimitPlan("fREE",
                               RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => plan must contain only letters and upperCase!"),
                Arguments.arguments(List.of(
                        new RSLimit(null, RequestMethod.POST, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "Url is null!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", null, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimit field => method is must not be null!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, null, null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimit field => plans is null or empty!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan(null,
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => plan must contain only letters and upperCase!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("null",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => plan must contain only letters and upperCase!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                null, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => requestType is must not be null!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                RequestType.NO_LIMIT, null, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => requestCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                RequestType.NO_LIMIT, -100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => requestCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                RequestType.NO_LIMIT, 100L, null, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => timeType is must not be null!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                RequestType.NO_LIMIT, 100L, TimeType.WEEK, null)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => timeCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                RequestType.NO_LIMIT, 100L, TimeType.WEEK, -1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.PUT, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "RSLimitPlan field => timeCount is less than one!"),
                Arguments.arguments(List.of(
                        new RSLimit("/api/check", RequestMethod.POST, List.of(new RSLimitPlan("NULL",
                                RequestType.NO_LIMIT, 100L, TimeType.WEEK, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null),
                        new RSLimit("/api/v1/jeck", RequestMethod.GET, List.of(new RSLimitPlan("FREE",
                                RequestType.LIMIT, 100L, TimeType.DAY, 1L)), null)
                ), "Duplicate Limits!")
        );
    }

}
