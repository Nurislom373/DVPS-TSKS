package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector
 * @since 8/19/2023 2:11 PM
 */
@Component(StateCollector.NAME)
public class StateCollector extends AbstractCollector implements Collector<Enum>{

    public static final String NAME = "stateCollector";

    public StateCollector(QuestMethod<Enum> questMethod) {
        super(questMethod);
    }

    @Override
    @SuppressWarnings("unchecked")
    public InvokerResult getInvokerResult(Object value, Enum param) {
        return questMethod.getMethodValueAnn(value, param);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasHandle(Enum param) {
        return questMethod.containsKey(param);
    }
}
