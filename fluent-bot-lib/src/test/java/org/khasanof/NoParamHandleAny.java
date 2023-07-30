package org.khasanof;

import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.main.annotation.UpdateController;
import org.khasanof.main.annotation.methods.HandleAny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.EnumSet;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 29.07.2023 16:29
 */
@UpdateController
public class NoParamHandleAny {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @HandleAny(type = HandleType.MESSAGE, proceed = Proceed.PROCEED)
    void handleNoParam(AbsSender sender, Update update) {
        System.out.println("Hello I'm logger method in test class!");
    }

}
