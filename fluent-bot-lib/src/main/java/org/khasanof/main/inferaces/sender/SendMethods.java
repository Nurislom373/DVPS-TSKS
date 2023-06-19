package org.khasanof.main.inferaces.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Author: Nurislom
 * <br/>
 * Date: 19.06.2023
 * <br/>
 * Time: 23:11
 * <br/>
 * Package: org.khasanof.main.inferaces.sender
 */
public interface SendMethods {

    void execute(SendMessage message);

}
