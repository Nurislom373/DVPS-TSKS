package org.khasanof.springbootstarterfluent.core.state.processor;

import org.khasanof.springbootstarterfluent.main.inferaces.ObjectCollector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.state.processor
 * @since 8/18/2023 5:46 AM
 */
@Component(StateCollector.NAME)
public class StateCollector implements ObjectCollector<Class<Enum>> {

    public static final String NAME = "stateCollector";
    public final Set<Class<Enum>> enums = new HashSet<>();

    @Override
    public Set<Class<Enum>> getAll() {
        return this.enums;
    }

    @Override
    public void addAll(Set<Class<Enum>> enums) {
        this.enums.addAll(enums);
    }

}
