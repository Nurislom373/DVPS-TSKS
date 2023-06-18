package org.khasanof.main.inferaces.executor;

import org.khasanof.core.enums.ExecutorType;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:59
 * <br/>
 * Package: org.khasanof.main.inferaces
 */
public interface Executor {

    void execute(Update update);

    ExecutorType getType();

}
