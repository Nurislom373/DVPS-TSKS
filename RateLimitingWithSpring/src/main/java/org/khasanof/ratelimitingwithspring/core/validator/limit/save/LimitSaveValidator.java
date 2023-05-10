package org.khasanof.ratelimitingwithspring.core.validator.limit.save;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimit;
import org.khasanof.ratelimitingwithspring.core.strategy.limit.classes.RSLimitPlan;
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
@RequiredArgsConstructor
public class LimitSaveValidator implements BaseValidator {

    public ValidatorResult validatorRSLimits(List<RSLimit> limits) {
        log.info("validatorRSLimits method started to be execute");
        Set<RSLimit> rsLimits = new HashSet<>(limits);
        return new ValidatorResult().success(Stream.of(Objects.equals(rsLimits.size(), limits.size()),
                checkRSLimits(limits).isSuccess()).allMatch(any -> any));
    }

    private ValidatorResult checkRSLimits(List<RSLimit> limits) {
        return new ValidatorResult().success(limits.stream()
                .map(this::checkRSLimit).allMatch(ThrowingPredicate::isTrue));
    }

    private ValidatorResult checkRSLimit(RSLimit limit) {
        return new ValidatorResult().success(Stream.of(checkUrl(limit), checkPlans(limit.getPlans()))
                .allMatch(ThrowingPredicate::isTrue));
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
        if (Objects.isNull(plan.getRequestCount()) || !StringUtils.isAllUpperCase(plan.getPlan())) {
            log.error("Plan must be all characters upper case! : {}", plan);
            return new ValidatorResult().failed("Plan must be all characters upper case!");
        }
        if (Objects.isNull(plan.getRequestCount()) || plan.getRequestCount() < 1) {
            log.error("RequestCount must be greater or equal than 1! : {}", plan);
            return new ValidatorResult().failed("RequestCount must be greater or equal than 1!");
        }
        if (Objects.isNull(plan.getTimeCount()) || plan.getTimeCount() < 1) {
            log.error("TimeCount must be greater or equal than 1! : {}", plan);
            return new ValidatorResult().failed("TimeCount must be greater or equal than 1!");
        }
        return new ValidatorResult().success(true);
    }


}
