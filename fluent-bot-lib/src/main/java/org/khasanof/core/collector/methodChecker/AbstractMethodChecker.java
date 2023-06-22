package org.khasanof.core.collector.methodChecker;

import org.khasanof.main.inferaces.sender.Sender;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.Method;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 23:23
 * <br/>
 * Package: org.khasanof.core.collector.methodChecker
 */
public abstract class AbstractMethodChecker {

    public abstract boolean valid(Method method);

    protected boolean paramsTypeCheck(Class<?>[] classes) {
        Class<?> firstParam = classes[0];
        boolean isSender = false, isUpdate = false, isAbsSender = false;
        if (firstParam.equals(Sender.class)) {
            if (classes.length == 1) {
                isSender = true;
            }
            return isSender;
        } else if (firstParam.equals(Update.class) || firstParam.equals(AbsSender.class)) {
            if (firstParam.equals(Update.class)) isUpdate = true;
            else isAbsSender = true;
            Class<?> secondParam = classes[1];
            if (secondParam.equals(Update.class) || secondParam.equals(AbsSender.class)) {
                if (secondParam.equals(Update.class)) isUpdate = true;
                else isAbsSender = true;
                return isAbsSender && isUpdate;
            }
        } else {
            return false;
        }
        return false;
    }

}
