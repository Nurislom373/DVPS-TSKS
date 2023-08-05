package org.khasanof.springbootstarterfluent.core.state;

import org.khasanof.springbootstarterfluent.core.exceptions.NotFoundException;
import org.khasanof.springbootstarterfluent.core.state.SimpleStateService;
import org.khasanof.springbootstarterfluent.main.inferaces.state.State;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 17:42
 */
public class StateRepository {

    private final SimpleStateService service = new SimpleStateService();
    private static final StateRepository instance = new StateRepository();
    private Map<Long, Enum> userConcurrentMap = new HashMap<>();

    public Enum getState(Long id) {
        Enum enumType = userConcurrentMap.get(id);
        if (Objects.isNull(enumType)) {
            throw new NotFoundException("State not found!");
        }
        return enumType;
    }

    public <T extends Enum> State<T> getState(Long id, Class<T> clazz) {
        return new State<>() {
            @Override
            public T getState() {
                return (T) userConcurrentMap.get(id);
            }

            @Override
            public void nextState() {
                userConcurrentMap.put(id, enumNextValue(service.getType(), getState()));
            }
        };
    }

    private Enum enumNextValue(Class<? extends Enum> enumType, Enum currentState) {
        boolean isCurrent = false;
        for (Enum enumConstant : enumType.getEnumConstants()) {
            if (isCurrent) {
                return enumConstant;
            }
            if (enumConstant.equals(currentState)) {
                isCurrent = true;
            }
        }
        throw new RuntimeException("next state not found!");
    }

    public int count() {
        return userConcurrentMap.size();
    }

    public boolean hasUserId(Long id) {
        return userConcurrentMap.containsKey(id);
    }

    public void addUser(User user) {
        userConcurrentMap.put(user.getId(), service.getType().getEnumConstants()[0]);
    }

    public static StateRepository getInstance() {
        return instance;
    }

}
