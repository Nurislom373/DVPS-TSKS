package org.khasanof.core.utils;

import lombok.SneakyThrows;
import org.khasanof.core.config.FluentConfig;
import org.khasanof.main.FluentBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 12.07.2023 23:45
 */
public abstract class UpdateUtils {

    public static Long getUserId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        } else {
            return update.getCallbackQuery().getFrom().getId();
        }
    }

    public static User getFrom(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        } else {
            return update.getCallbackQuery().getFrom();
        }
    }

    public static String getFileId(Update update) {
        Map<Function<Message, Boolean>, Function<Message, Object>> functionMap = new HashMap<>() {{
            put((message -> message.getClass().equals(Message.class)), (Message::getDocument));
            put((Message::hasAudio), (Message::getAudio));
            put((Message::hasPhoto), (Message::getPhoto));
            put((Message::hasVideo), (Message::getVideo));
            put((Message::hasVideoNote), (Message::getVideoNote));
        }};
        return getListMatchFunctionValue(update.getMessage(), functionMap, "fileId");
    }

    @SneakyThrows
    public static InputStream getInputStreamWithFileId(String id) {
        GetFile file = new GetFile();
        file.setFileId(id);
        File executed = FluentBot.getInstance().execute(file);
        return new URL(executed.getFileUrl(FluentConfig.getInstance().getConfig().getToken()))
                .openStream();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getListMatchFunctionValue(Message message, Map<Function<Message, Boolean>, Function<Message, Object>> functionMap,
                                                   String fieldName) {
        return (T) functionMap.entrySet().stream().filter(functionFunctionEntry ->
                functionFunctionEntry.getKey().apply(message)).findFirst()
                .map(functionFunctionEntry -> getObjField(functionFunctionEntry.getValue().apply(message),
                        fieldName)).orElse(null);
    }

    public static <T, R> R getListMatchFunctionValue(T message, Map<Function<T, Boolean>, Function<T, R>> functionMap) {
        return functionMap.entrySet().stream().filter(functionFunctionEntry ->
                        functionFunctionEntry.getKey().apply(message)).findFirst()
                .map(functionFunctionEntry -> functionFunctionEntry.getValue().apply(message))
                .orElse(null);
    }

    @SneakyThrows
    private static Object getObjField(Object obj, String fieldName) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().equals(fieldName)) {
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            }
        }
        throw new RuntimeException("field not found!");
    }

}
