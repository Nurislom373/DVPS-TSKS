package org.khasanof.core.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 17:29
 */
@Getter
@Setter
@AllArgsConstructor
public class StateCore {
    private String currentState;
    private String prevState;
    private String mainState;
}
