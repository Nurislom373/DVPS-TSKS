package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.determination
 * @since 8/5/2023 9:02 PM
 */
public interface DeterminationService {

    List<BiConsumer<Update, Map<Method, Object>>> getDeterminations();

}
