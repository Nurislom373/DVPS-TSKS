package org.khasanof.core.executors;

import org.khasanof.core.model.MethodArgs;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.khasanof.main.FluentBot.getInstance;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors
 * @since 24.06.2023 0:46
 * @version 1.0.5
 */
public class CommonUpdateExecutor extends AbstractExecutor {

    private final DeterminationUpdateType determinationUpdateType = new DeterminationUpdateType(collector);

    public void execute(Update update) {
        determinationUpdateType.determination(update)
                .entrySet().forEach((entry) -> invoke(entry, new MethodArgs(update, getInstance())));
    }
}
