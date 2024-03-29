package org.khasanof.springbootstarterfluent.main.inferaces;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:38
 * <br/>
 * Package: org.khasanof.main.inferaces
 */
public interface MainHandler {

    void process(Update update);

}
