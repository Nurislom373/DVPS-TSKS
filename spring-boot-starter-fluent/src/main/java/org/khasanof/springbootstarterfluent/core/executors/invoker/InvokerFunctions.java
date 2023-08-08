package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.utils.AnnotationUtils;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.core.utils.UpdateUtils;
import org.khasanof.springbootstarterfluent.main.FluentBot;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessFile;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.invoker
 * @since 15.07.2023 23:59
 */
@Component
public class InvokerFunctions implements ApplicationContextAware {

    private final Set<InvokerModel> invokerModels = new LinkedHashSet<>();

    public static final String EXCEPTION_NAME = "handleException";
    public static final String HANDLE_UPDATE = "handleUpdate";
    public static final String HANDLE_UPDATE_W_PROCESS_FL = "handleUpdateWithProcessFile";
    public static final String HANDLE_ANY_UPDATE = "handleAnyUpdate";
    public static final String HANDLE_STATE = "handleState";

    public void addInvokerModel(InvokerModel invokerModel) {
        invokerModels.add(invokerModel);
    }

    public InvokerModel fillAndGet(Map.Entry<Method, Object> entry, Object... args) {
        InvokerModel model = invokerModels.stream().filter(invokerModel -> AnnotationUtils.hasAnnotation(entry.getKey(),
                        invokerModel.getAnnotation(), invokerModel.isSuperAnnotation()))
                .findFirst().orElseThrow(() -> new RuntimeException("InvokerModel not found!"));

        List<Object> objects = new ArrayList<>();
        if (model.isHasMainParam() && !model.isInputSystem()) {
            if (model.getName().equals(HANDLE_UPDATE_W_PROCESS_FL)) {
                int parameterCount = entry.getKey().getParameterCount();
                if (parameterCount > 2) {
                    mainParamAddArgsArray(model, objects, args);
                }
            } else {
                mainParamAddArgsArray(model, objects, args);
            }
        }

        objects.addAll(Arrays.stream(args).toList());
        model.setArgs(objects.toArray());

        return model.methodSClass(entry);
    }

    private static void mainParamAddArgsArray(InvokerModel model, List<Object> objects, Object[] args) {
        Object apply = model.getMainParam().getValueFunction()
                .apply(MethodUtils.getArg(args, Update.class));
        objects.add(apply);
    }

    public InvokerModel getInvokerByName(String name) {
        return invokerModels.stream().filter(invokerModel ->
                        invokerModel.getName().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("Invoker not found!"));
    }

    private void addDefaultInvokers() {
        // TODO this class is one of the state classes
        /*List<Class<?>> classList1 = List.of(Update.class, AbsSender.class, State.class);
        InvokerModel invokerModel3 = new InvokerModel(HANDLE_STATE, false, HandleState.class,
                classList1, true, new InvokerModel.MainParam(
                (update -> stateRepository.getState(UpdateUtils.getUserId(update), stateService.getType()))));
        addInvokerModel(invokerModel3);*/
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<Class<?>> classList = List.of(Update.class, AbsSender.class);
        InvokerModel handleAnyModel = new InvokerModel(HANDLE_ANY_UPDATE, false, HandleAny.class,
                classList, false, null, true);
        addInvokerModel(handleAnyModel);

        List<Class<?>> classList3 = List.of(Update.class, AbsSender.class, Throwable.class);
        InvokerModel invokerModel3 = new InvokerModel(EXCEPTION_NAME, false, HandleException.class,
                classList3, true, true);
        addInvokerModel(invokerModel3);

        List<Class<?>> classList4 = List.of(Update.class, AbsSender.class, InputStream.class);
        InvokerModel invokerModel4 = new InvokerModel(HANDLE_UPDATE_W_PROCESS_FL, true, ProcessFile.class,
                classList4, true, new InvokerModel.MainParam((update ->
                UpdateUtils.getInputStreamWithFileId(UpdateUtils.getFileId(update),
                        applicationContext.getBean(FluentBot.class)))));
        addInvokerModel(invokerModel4);

        List<Class<?>> classList2 = List.of(Update.class, AbsSender.class);
        InvokerModel invokerModel2 = new InvokerModel(HANDLE_UPDATE, true, ProcessUpdate.class,
                classList2, false, null);
        addInvokerModel(invokerModel2);
    }
}
