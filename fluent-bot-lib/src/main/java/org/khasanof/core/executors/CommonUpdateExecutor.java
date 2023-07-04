package org.khasanof.core.executors;

import org.khasanof.core.custom.BreakerForEach;
import org.khasanof.core.custom.FluentContext;
import org.khasanof.core.model.MethodArgs;
import org.khasanof.main.FluentBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @version 1.0.5
 * @see org.khasanof.core.executors
 * @since 24.06.2023 0:46
 */
public class CommonUpdateExecutor extends AbstractExecutor {

    private final DeterminationUpdateType determinationUpdateType = new DeterminationUpdateType(collector);

    public void execute(Update update) {
        BreakerForEach.forEach(determinationUpdateType.determination(update).entrySet().stream(),
                ((entry, breaker) -> {
                    if (!FluentContext.booleanLocal.get()) {
                        invoke(entry, new MethodArgs(update, FluentBot.getInstance()));
                    } else {
                        breaker.stop();
                    }
                }));
    }
}
