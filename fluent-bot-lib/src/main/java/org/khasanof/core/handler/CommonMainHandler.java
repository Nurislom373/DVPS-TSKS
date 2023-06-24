package org.khasanof.core.handler;

import org.khasanof.core.executors.CommonUpdateExecutor;
import org.khasanof.main.inferaces.MainHandler;
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
    private final CommonUpdateExecutor executor = new CommonUpdateExecutor();

    @Override
    public void process(Update update) {
        executorService.execute(() -> executor.execute(update));
    }

    public static MainHandler getInstance() {
        if (mainHandler == null) {
            mainHandler = new CommonMainHandler();
        }
        return mainHandler;
    }

}
