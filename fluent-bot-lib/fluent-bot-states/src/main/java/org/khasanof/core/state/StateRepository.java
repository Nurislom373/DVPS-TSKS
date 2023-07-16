package org.khasanof.core.state;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 17:42
 */
public class StateRepository {

    private static final StateRepository instance = new StateRepository();
    private final Map<Long, StateCore> userConcurrentMap = new HashMap<>();

    public StateCore userGetState(Long id) {
        return userConcurrentMap.get(id);
    }

    public SimpleState getSimpleState(Long id) {
        return new SimpleState(userConcurrentMap.get(id));
    }

    public int count() {
        return userConcurrentMap.size();
    }

    public boolean hasUserId(Long id) {
        return userConcurrentMap.containsKey(id);
    }

    public void addUser(User user) {
        userConcurrentMap.put(user.getId(), new StateCore("START", "START", "START"));
    }

    public static StateRepository getInstance() {
        return instance;
    }

}
