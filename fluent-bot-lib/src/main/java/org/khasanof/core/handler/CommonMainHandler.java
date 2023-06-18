package org.khasanof.core.handler;

import org.khasanof.core.executors.CommonCallbackExecutor;
import org.khasanof.core.executors.CommonMessageExecutor;
import org.khasanof.main.inferaces.executor.CallbackExecutor;
import org.khasanof.main.inferaces.MainHandler;
import org.khasanof.main.inferaces.executor.MessageExecutor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:50
 * <br/>
 * Package: org.khasanof.core.handler
 */
public class CommonMainHandler implements MainHandler {

    private static MainHandler mainHandler;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final CallbackExecutor callbackExecutor = new CommonCallbackExecutor();
    private final MessageExecutor messageExecutor = new CommonMessageExecutor();

    @Override
    public void process(Update update) {
        executorService.execute(() -> {
            if (update.hasMessage()) messageExecutor.execute(update);
            else if (update.hasCallbackQuery()) callbackExecutor.execute(update);
        });
    }

    public static MainHandler getInstance() {
        if (mainHandler == null) {
            mainHandler = new CommonMainHandler();
        }
        return mainHandler;
    }

}
