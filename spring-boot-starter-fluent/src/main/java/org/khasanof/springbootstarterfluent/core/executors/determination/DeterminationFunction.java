package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.khasanof.springbootstarterfluent.core.config.DefineProcessType;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination
 * @since 16.07.2023 17:43
 */
public interface DeterminationFunction extends DefineProcessType {

    BiConsumer<Update, Set<InvokerResult>> accept(ApplicationContext applicationContext);

    Integer getOrder();

}
