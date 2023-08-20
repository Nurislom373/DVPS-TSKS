package org.khasanof.springbootstarterfluent.core.state;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.exceptions.InvalidParamsException;
import org.khasanof.springbootstarterfluent.core.exceptions.InvalidValidationException;
import org.khasanof.springbootstarterfluent.main.inferaces.ObjectCollector;
import org.khasanof.springbootstarterfluent.main.inferaces.ObjectContains;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state
 * @since 8/19/2023 12:31 AM
 */
@Slf4j
@Component
public class StateValidator {

    private final ObjectContains<Enum> objectContains;

    public StateValidator(ObjectContains<Enum> objectContains) {
        this.objectContains = objectContains;
    }

    public boolean valid(StateActions stateActions) {
        if (Objects.isNull(stateActions.state())) {
            log.warn("state must not be null in this class : {}", stateActions.getClass());
            throw new InvalidValidationException("state must not be null!");
        }
        if (!objectContains.containsEnum(stateActions.state().getClass())) {
            log.warn("An unregistered enum type was introduced! : {}", stateActions.getClass());
            throw new RuntimeException("An unregistered enum type was introduced");
        }
        return true;
    }

}
