package org.khasanof.springbootstarterfluent.core.event.exceptionDirector;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.event.exceptionDirector
 * @since 8/20/2023 5:53 PM
 */
@Getter
@Setter
@ToString
public class ExceptionDirectorEvent extends ApplicationEvent {

    private Update update;
    private AbsSender absSender;
    private Throwable throwable;

    public ExceptionDirectorEvent(Object source, Update update, AbsSender absSender, Throwable throwable) {
        super(source);
        this.update = update;
        this.absSender = absSender;
        this.throwable = throwable;
    }
}
