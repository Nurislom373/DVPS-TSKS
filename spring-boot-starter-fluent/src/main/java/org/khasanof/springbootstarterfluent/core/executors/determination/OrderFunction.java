package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination
 * @since 16.07.2023 17:43
 */
public interface OrderFunction {

    BiConsumer<Update, Map<Method, Object>> accept(List<Object> list);

    Integer getOrder();

}
