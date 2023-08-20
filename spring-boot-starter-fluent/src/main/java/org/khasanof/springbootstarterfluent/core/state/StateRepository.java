package org.khasanof.springbootstarterfluent.core.state;

import org.khasanof.springbootstarterfluent.core.exceptions.NotFoundException;
import org.khasanof.springbootstarterfluent.core.state.processor.StateEnumsCollector;
import org.khasanof.springbootstarterfluent.main.inferaces.state.State;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 17:42
 */
@Repository
public class StateRepository {

    public Map<Long, State> userConcurrentMap = new HashMap<>();
    private final StateEnumsCollector statesCollector;

    public StateRepository(StateEnumsCollector statesCollector) {
        this.statesCollector = statesCollector;
    }

    public Enum getState(Long id) {
        Enum enumType = userConcurrentMap.get(id).getState();
        if (Objects.isNull(enumType)) {
            throw new NotFoundException("State not found!");
        }
        return enumType;
    }

    public State getStateById(Long id) {
        return userConcurrentMap.get(id);
    }

    public void nextState(Long id) {
        userConcurrentMap.get(id).nextState();
    }

    public boolean hasUserId(Long id) {
        return userConcurrentMap.containsKey(id);
    }

    public void addUser(User user) {
        userConcurrentMap.put(user.getId(), new StateImpl(statesCollector.enums.iterator().next()
                .getEnumConstants()[0]));
    }

}
