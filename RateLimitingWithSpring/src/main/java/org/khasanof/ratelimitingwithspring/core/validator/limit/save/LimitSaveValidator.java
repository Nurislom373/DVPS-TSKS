package org.khasanof.ratelimitingwithspring.core.validator.limit.save;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.khasanof.ratelimitingwithspring.core.exceptions.InvalidValidationException;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimitPlan;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.khasanof.ratelimitingwithspring.core.validator.BaseValidator;
import org.khasanof.ratelimitingwithspring.core.utils.functional.ThrowingPredicate;
import org.khasanof.ratelimitingwithspring.core.validator.ValidatorResult;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 5:38 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.validator.limit.save
 */
@Slf4j
@Component
public class LimitSaveValidator implements BaseValidator {

    public ValidatorResult validatorRSLimits(List<RSLimit> limits) {
        log.info("validatorRSLimits method started to be execute");
        if (limits != null && limits.size() >= 1) {
            ValidatorResult validatorResult = checkRSLimits(limits);
            if (validatorResult.isSuccess() && limits.size() > 1) {
                if (checkDuplicates(limits)) {
                    return new ValidatorResult().success(true);
                } else {
                    throw new InvalidValidationException("Duplicate Limits!");
                }
            } else if (validatorResult.isSuccess()) {
                return new ValidatorResult().success(true);
            } else {
                throw new InvalidValidationException("Invalid Validation Exception!");
            }
        }
        throw new InvalidValidationException("List is Empty");
    }

    private ValidatorResult checkRSLimits(List<RSLimit> limits) {
        return new ValidatorResult().success(limits.stream()
                .map(this::checkRSLimit).allMatch(ValidatorResult::isSuccess));
    }

    private ValidatorResult checkRSLimit(RSLimit limit) {
        return new ValidatorResult().success(Stream.of(checkFields(limit),
                        checkPlans(limit.getPlans()))
                .allMatch(ThrowingPredicate::isTrue));
    }

    private ValidatorResult checkFields(RSLimit limit) {
        if (Objects.isNull(limit.getMethod())) {
            log.error("RSLimit field => method is must not be null! : {}", limit);
            return new ValidatorResult().failed("RSLimit field => method is must not be null!");
        }
        return checkUrl(limit);
    }

    private ValidatorResult checkUrl(RSLimit limit) {
        if (Objects.nonNull(limit.getUrl()) && !limit.getUrl().isBlank()) {
            if (limit.getVariables() != null && !limit.getVariables().isEmpty()) {
                if (limit.getUrl().endsWith("*")) {
                    return new ValidatorResult().success(true);
                } else {
                    log.error("Url must end with * because there are variables in it! : {}", limit.getUrl());
                    return new ValidatorResult().failed("Url must end with * because there are variables in it!");
                }
            } else {
                return new ValidatorResult().success(true);
            }
        } else {
            log.error("Url is null! : {}", limit);
            return new ValidatorResult().failed("Url is null!");
        }
    }

    private ValidatorResult checkPlans(List<RSLimitPlan> plans) {
        return new ValidatorResult().success(plans.stream()
                .map(this::checkPlan)
                .allMatch(ThrowingPredicate::isTrue));
    }

    private ValidatorResult checkPlan(RSLimitPlan plan) {
        if (Objects.isNull(plan.getRequestType())) {
            log.error("RSLimitPlan field => requestType is must not be null! : {}", plan);
            return new ValidatorResult().failed("RSLimitPlan field => requestType is must not be null!");
        }
        if (Objects.isNull(plan.getTimeType())) {
            log.error("RSLimitPlan field => timeType is must not be null! : {}", plan);
            return new ValidatorResult().failed("RSLimitPlan field => timeType is must not be null!");
        }
        if (Objects.isNull(plan.getPlan()) || !StringUtils.isAllUpperCase(plan.getPlan())) {
            log.error("RSLimitPlan field => plan must contain only letters and upperCase! : {}", plan);
            return new ValidatorResult().failed("RSLimitPlan field => plan must contain only letters and upperCase!");
        }
        if (Objects.isNull(plan.getRequestCount()) || plan.getRequestCount() < 1) {
            log.error("RSLimitPlan field => requestCount is less than one! : {}", plan);
            return new ValidatorResult().failed("RSLimitPlan field => requestCount is less than one!");
        }
        if (Objects.isNull(plan.getTimeCount()) || plan.getTimeCount() < 1) {
            log.error("RSLimitPlan field => timeCount is less than one! : {}", plan);
            return new ValidatorResult().failed("RSLimitPlan field => timeCount is less than one!");
        }
        return new ValidatorResult().success(true);
    }

    private boolean checkDuplicates(List<RSLimit> limits) {
        return limits.stream().map(var1 -> limits.stream()
                .filter(var2 -> areEqual(var1, var2)).count())
                .allMatch(match -> match == 1);
    }

    private boolean areEqual(RSLimit limit1, RSLimit limit2) {
        if (limit1.getUrl().equals(limit2.getUrl())) {
            if (limit1.getMethod().equals(limit2.getMethod())) {
                if (Objects.nonNull(limit1.getVariables()) &&  Objects.nonNull(limit2.getVariables())) {
                    return BaseUtils.areEqual(limit1.getVariables(), limit2.getVariables());
                }
                return Objects.isNull(limit1.getVariables()) && Objects.isNull(limit2.getVariables());
            }
        }
        return false;
    }


}
