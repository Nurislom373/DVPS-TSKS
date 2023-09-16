package org.khasanof.springbootstarterfluent.core.executors.determination.impls;

import org.khasanof.condition.Condition;
import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.collector.StateCollector;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.executors.determination.OrderFunction;
import org.khasanof.springbootstarterfluent.core.model.InvokerObject;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.khasanof.springbootstarterfluent.core.state.StateActions;
import org.khasanof.springbootstarterfluent.core.state.StateRepository;
import org.khasanof.springbootstarterfluent.core.utils.UpdateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination.impls
 * @since 16.07.2023 19:05
 */
@Component(HandleStateFunction.NAME)
public class HandleStateFunction implements OrderFunction {

    public static final String NAME = "handleStateFunction";

    @Override
    @SuppressWarnings("unchecked")
    public BiConsumer<Update, Set<InvokerResult>> accept(ApplicationContext applicationContext) {
        return ((update, invokerResults) -> {
            StateRepository repository = applicationContext.getBean(StateRepository.class);
            Long id = UpdateUtils.getUserId(update);

            Condition.isFalseThen(repository.hasUserId(id))
                    .thenCall(() -> repository.addUser(UpdateUtils.getFrom(update)));

            Enum state = repository.getState(id);
            if (Objects.nonNull(state)) {
                Collector<Enum> collector = applicationContext.getBean(StateCollector.NAME, Collector.class);
                InvokerResult classEntry = collector.getInvokerResult(state, state);

                Condition.isTrue(Objects.nonNull(classEntry))
                        .thenCall(() -> {
                            invokerResults.add(classEntry);
                            Condition.isTrueThen(isNotProcessedUpdates(classEntry))
                                    .thenCall(() -> {
                                        FluentContext.determinationServiceBoolean.set(true);
                                    });
                        })
                        .elseDoNothing();
            }
        });
    }

    private boolean isNotProcessedUpdates(InvokerResult result) {
        InvokerObject invokerObject = (InvokerObject) result;
        StateActions stateActions = (StateActions) invokerObject.getReference();
        return !stateActions.updateHandlersProceed();
    }

    @Override
    public Integer getOrder() {
        return 5;
    }

    @Override
    public ProcessType processType() {
        return ProcessType.STATE;
    }
}
