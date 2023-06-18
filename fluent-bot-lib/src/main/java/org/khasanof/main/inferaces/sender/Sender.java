package org.khasanof.main.inferaces.sender;

import org.khasanof.core.sender.MessageBuilder;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 13:04
 * <br/>
 * Package: org.khasanof.main.inferaces.sender
 */
public interface Sender {

    void execute(MessageBuilder messageBuilder);

    void execute(String message);

    void execute(String message, String parseMode);

}
