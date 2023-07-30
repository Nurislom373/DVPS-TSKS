package org.khasanof.core.executors.invoker;

import org.khasanof.core.config.FluentConfig;
import org.khasanof.core.model.InvokerModel;
import org.khasanof.core.state.SimpleStateService;
import org.khasanof.core.state.StateRepository;
import org.khasanof.core.utils.AnnotationUtils;
import org.khasanof.core.utils.MethodUtils;
import org.khasanof.core.utils.UpdateUtils;
import org.khasanof.main.annotation.exception.HandleException;
import org.khasanof.main.annotation.extra.HandleState;
import org.khasanof.main.annotation.methods.HandleAny;
import org.khasanof.main.annotation.process.ProcessUpdate;
import org.khasanof.main.inferaces.state.State;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.invoker
 * @since 15.07.2023 23:59
 */
public class InvokerFunctions {

    private final Set<InvokerModel> invokerModels = new HashSet<>();

    public static final String EXCEPTION_NAME = "handleException";
    public static final String HANDLE_UPDATE = "handleUpdate";
    public static final String HANDLE_ANY_UPDATE = "handleAnyUpdate";
    public static final String HANDLE_STATE = "handleState";


    public InvokerFunctions() {
        addDefaultInvokers();
    }

    public void addInvokerModel(InvokerModel invokerModel) {
        invokerModels.add(invokerModel);
    }

    public InvokerModel fillAndGet(Map.Entry<Method, Class> entry, Object... args) {
        InvokerModel model = invokerModels.stream().filter(invokerModel -> AnnotationUtils.hasAnnotation(entry.getKey(),
                        invokerModel.getAnnotation(), invokerModel.isSuperAnnotation()))
                .findFirst().orElseThrow(() -> new RuntimeException("InvokerModel not found!"));

        List<Object> objects = new ArrayList<>();
        if (model.isHasMainParam() && !model.isInputSystem()) {
            Object apply = model.getMainParam().getValueFunction()
                    .apply(MethodUtils.getArg(args, Update.class));
            objects.add(apply);
        }

        objects.addAll(Arrays.stream(args).toList());
        model.setArgs(objects.toArray());

        return model.methodSClass(entry);
    }

    public InvokerModel getInvokerByName(String name) {
        return invokerModels.stream().filter(invokerModel ->
                        invokerModel.getName().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("Invoker not found!"));
    }

    private void addDefaultInvokers() {
        List<Class<?>> classList = List.of(Update.class, AbsSender.class);
        InvokerModel handleAnyModel = new InvokerModel(HANDLE_ANY_UPDATE, false, HandleAny.class,
                classList, false, null, true);
        addInvokerModel(handleAnyModel);

        List<Class<?>> classList2 = List.of(Update.class, AbsSender.class);
        InvokerModel invokerModel2 = new InvokerModel(HANDLE_UPDATE, true, ProcessUpdate.class,
                classList2, false, null);
        addInvokerModel(invokerModel2);

        // TODO this class is one of the state classes
        /*List<Class<?>> classList1 = List.of(Update.class, AbsSender.class, State.class);
        InvokerModel invokerModel3 = new InvokerModel(HANDLE_STATE, false, HandleState.class,
                classList1, true, new InvokerModel.MainParam(
                (update -> stateRepository.getState(UpdateUtils.getUserId(update), stateService.getType()))));
        addInvokerModel(invokerModel3);*/

        List<Class<?>> classList3 = List.of(Update.class, AbsSender.class, Throwable.class);
        InvokerModel invokerModel3 = new InvokerModel(EXCEPTION_NAME, false, HandleException.class,
                classList3, true, true);
        addInvokerModel(invokerModel3);
    }

}
