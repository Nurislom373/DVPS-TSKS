package org.khasanof.core.sender;

import lombok.RequiredArgsConstructor;
import org.khasanof.core.enums.ExecutorType;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 15:44
 * <br/>
 * Package: org.khasanof.core.sender
 */
@RequiredArgsConstructor
public abstract class AbstractSender {
    protected final ExecutorType type;
    protected final Update update;
    protected final AbsSender sender;
    protected final SimpleSenderUtil util;
}
