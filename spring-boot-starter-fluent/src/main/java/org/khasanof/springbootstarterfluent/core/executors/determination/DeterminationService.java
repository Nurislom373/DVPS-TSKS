package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.determination
 * @since 8/5/2023 9:02 PM
 */
public interface DeterminationService {

    List<BiConsumer<Update, Set<InvokerResult>>> getDeterminations();

}
