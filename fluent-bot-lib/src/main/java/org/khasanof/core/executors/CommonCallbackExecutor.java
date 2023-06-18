package org.khasanof.core.executors;

import org.khasanof.core.enums.ExecutorType;
import org.khasanof.core.sender.SimpleSender;
import org.khasanof.main.FluentBot;
import org.khasanof.main.annotation.HandleCallback;
import org.khasanof.main.inferaces.executor.CallbackExecutor;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 11:16
 * <br/>
 * Package: org.khasanof.core.executors
 */
public class CommonCallbackExecutor extends AbstractExecutor implements CallbackExecutor {

    @Override
    public void execute(Update update) {
        invoke(collector.getMethodValueAnn(update.getCallbackQuery().getData(), HandleCallback.class),
                new SimpleSender(update, FluentBot.getInstance(), getType()));
    }

    @Override
    public ExecutorType getType() {
        return ExecutorType.CALLBACK;
    }

}
