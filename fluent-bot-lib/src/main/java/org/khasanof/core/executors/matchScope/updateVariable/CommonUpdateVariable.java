package org.khasanof.core.executors.matchScope.updateVariable;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matchScope.updateVariable
 * @since 30.07.2023 20:59
 */
@RequiredArgsConstructor
public class CommonUpdateVariable {

    private final Update update;
    private final String updateVarExpression;
    private final boolean isCallback;

    // TODO will finish!
    public boolean matchUpdateVariable() {
        return false;
    }

}
