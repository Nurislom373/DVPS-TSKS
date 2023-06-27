package org.khasanof.core.executors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.khasanof.core.enums.HandleType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 27.06.2023 21:52
 */
@Getter
@RequiredArgsConstructor
public enum MatchFunctions {

    HAS_TEXT((update -> setFunction(update.getMessage(), Message::hasText, Message::getText, HandleType.MESSAGE)), MatchType.MESSAGE),
    HAS_AUDIO((update -> setFunction(update.getMessage(), Message::hasAudio, Message::getAudio, HandleType.AUDIO)), MatchType.MESSAGE),
    HAS_VIDEO((update -> setFunction(update.getMessage(), Message::hasVideo, Message::getVideo, HandleType.VIDEO)), MatchType.MESSAGE);

    private final Function<Update, RecordFunction> method;
    private final MatchType matchType;

    public static Set<Function<Update, RecordFunction>> getMatchTypeFunctions(MatchType type) {
        return Arrays.stream(values()).filter(matchFunctions -> matchFunctions.matchType.equals(type))
                .map(MatchFunctions::getMethod).collect(Collectors.toSet());
    }

    public static <T> RecordFunction setFunction(T message, Function<T, Boolean> booleanFunction, Function<T, Object> objectFunction,
                                                 HandleType type) {
        return new RecordFunction(Map.entry(booleanFunction.apply(message), () -> Map.entry(Map.entry(objectFunction.apply(message)
                .getClass(), type), objectFunction.apply(message))));
    }

    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    enum MatchType {

        MESSAGE(Update::hasMessage, true),
        CALLBACK(Update::hasCallbackQuery, false,
                (update -> setSupplyMethod(update.getCallbackQuery(), HandleType.CALLBACK)));

        private final Function<Update, Boolean> method;
        private final boolean hasSubFunctions;
        private Function<Update, Map.Entry<Map.Entry<Class<?>, HandleType>, Object>> supplyMethod;

        public static MatchType getMatchType(Update update) {
            return Arrays.stream(values()).filter(matchType -> matchType.method.apply(update))
                    .findFirst().orElse(null);
        }

        public static <T> Map.Entry<Map.Entry<Class<?>, HandleType>, Object> setSupplyMethod(T data, HandleType type) {
            return Map.entry(Map.entry(data.getClass(), type), data);
        }

    }

}
