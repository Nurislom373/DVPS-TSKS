package org.khasanof.core.executors;

import org.khasanof.core.enums.ExecutorType;
import org.khasanof.core.sender.SimpleSender;
import org.khasanof.main.FluentBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof.core.executors
 * @since 24.06.2023 0:46
 * @version 1.0.5
 */
public class CommonUpdateExecutor extends AbstractExecutor {

    private final DeterminationUpdateType determinationUpdateType = new DeterminationUpdateType(collector);

    public void execute(Update update) {
        Map.Entry<Map.Entry<Method, Class>, ExecutorType> typeEntry = determinationUpdateType.determination(update);
        invoke(typeEntry.getKey(), new SimpleSender(update, FluentBot.getInstance(), typeEntry.getValue()));
    }
}
