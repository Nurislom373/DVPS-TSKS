package org.khasanof.springbootstarterfluent.core.executors;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.executors.determination.impls.HandleAnyFunction;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 27.06.2023 21:32
 */
@Slf4j
@Component(HandleAnyFunctionMatcher.NAME)
public class HandleAnyFunctionMatcher {

    public static final String NAME = "handleAnyFunctionMatcher";
    final Function<Update, MatchFunctions.MatchType> function = MatchFunctions.MatchType::getMatchType;

    public Map.Entry<HandleType, Object> matchFunctions(Update update) {
        MatchFunctions.MatchType matchType = function.apply(update);
        System.out.println("MatchType function result : " + matchType);
        if (Objects.nonNull(matchType)) {
            if (!matchType.isHasSubFunctions()) {
                System.out.println("No Sub Functions : " + matchType);
                return matchType.getSupplyMethod().apply(update);
            } else {
                System.out.println("Yes Sub Functions : " + matchType);
                return MatchFunctions.getMatchTypeFunctions(matchType)
                        .stream().filter(matchFun -> matchFun.apply(update).supplierEntry().getKey())
                        .map(updateRecordFunctionFunction -> updateRecordFunctionFunction.apply(update)
                                .supplierEntry().getValue().get())
                        .findFirst().orElse(null);
            }
        }
        return null;
    }

}
