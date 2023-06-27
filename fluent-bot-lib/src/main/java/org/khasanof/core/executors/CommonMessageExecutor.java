package org.khasanof.core.executors;

import org.khasanof.core.enums.ExecutorType;
import org.khasanof.core.model.MethodArgs;
import org.khasanof.core.sender.SimpleSender;
import org.khasanof.main.FluentBot;
import org.khasanof.main.annotation.methods.HandleMessage;
import org.khasanof.main.inferaces.executor.MessageExecutor;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.khasanof.main.FluentBot.getInstance;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:15
 * <br/>
 * Package: org.khasanof.core.executors
 */
@Deprecated
public class CommonMessageExecutor extends AbstractExecutor implements MessageExecutor {

    @Override
    public void execute(Update update) {
        if (update.getMessage().hasText()) {
            invoke(collector.getMethodValueAnn(update.getMessage().getText().trim(), HandleMessage.class),
                    new MethodArgs(update, getInstance()));
        }
    }

    @Override
    public ExecutorType getType() {
        return ExecutorType.MESSAGE;
    }
}
