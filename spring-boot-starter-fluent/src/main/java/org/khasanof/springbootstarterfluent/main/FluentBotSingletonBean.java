package org.khasanof.springbootstarterfluent.main;

import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 12/10/2023 12:45 PM
 */
@Component(FluentBotSingletonBean.NAME)
public class FluentBotSingletonBean extends GenericSingleton<FluentBot> {

    public static final String NAME = "fluentBotSingletonBean";

}
