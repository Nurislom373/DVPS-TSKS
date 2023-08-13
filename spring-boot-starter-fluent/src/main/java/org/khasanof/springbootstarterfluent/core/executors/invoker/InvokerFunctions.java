package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.executors.invoker.additional.checks.CWTCommonAdapter;
import org.khasanof.springbootstarterfluent.core.executors.invoker.additional.param.TWTCommonAdapter;
import org.khasanof.springbootstarterfluent.core.model.AdditionalParam;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.utils.AnnotationUtils;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.khasanof.springbootstarterfluent.core.executors.invoker.DefaultInvokerFunctions.HANDLE_UPDATE_W_PROCESS_FL;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.invoker
 * @since 15.07.2023 23:59
 */
@Component
public class InvokerFunctions {

    private final Set<InvokerModel> invokerModels = new LinkedHashSet<>();
    private final CWTCommonAdapter cwtCommonAdapter;
    private final TWTCommonAdapter twtCommonAdapter;

    public InvokerFunctions(CWTCommonAdapter cwtCommonAdapter, TWTCommonAdapter twtCommonAdapter) {
        this.cwtCommonAdapter = cwtCommonAdapter;
        this.twtCommonAdapter = twtCommonAdapter;
    }

    public void addInvokerModel(InvokerModel invokerModel) {
        invokerModels.add(invokerModel);
    }

    public InvokerModel fillAndGet(Map.Entry<Method, Object> entry, Object... args) {
        InvokerModel model = invokerModels.stream().filter(invokerModel -> matchInvokerModel(invokerModel, entry))
                .findFirst().orElseThrow(() -> new RuntimeException("InvokerModel not found!"));

        List<Object> objects = new ArrayList<>();
        if (model.isHasMainParam() && !model.isInputSystem()) {
            if (model.getName().equals(HANDLE_UPDATE_W_PROCESS_FL)) {
                int parameterCount = entry.getKey().getParameterCount();
                if (parameterCount > 2) {
                    mainParamAddArgsArray(model, objects, args, entry.getKey());
                }
            } else {
                mainParamAddArgsArray(model, objects, args, entry.getKey());
            }
        }

        objects.addAll(Arrays.stream(args).filter(Objects::nonNull).toList());
        model.setArgs(objects.toArray());

        return model.methodSClass(entry);
    }

    private boolean matchInvokerModel(InvokerModel invokerModel, Map.Entry<Method, Object> entry) {
        if (!hasExtraChecks(invokerModel)) {
            return annotationPresent(invokerModel, entry);
        } else {
            invokerModel.setClassEntry(entry);
            return annotationPresent(invokerModel, entry) && cwtCommonAdapter.check(invokerModel);
        }
    }

    private boolean annotationPresent(InvokerModel invokerModel, Map.Entry<Method, Object> entry) {
        return AnnotationUtils.hasAnnotation(entry.getKey(),
                invokerModel.getAnnotation(), invokerModel.isSuperAnnotation());
    }

    private boolean hasExtraChecks(InvokerModel invokerModel) {
        return Objects.nonNull(invokerModel.getAdditionalChecks());
    }

    private void mainParamAddArgsArray(InvokerModel model, List<Object> objects, Object[] args, Method method) {
        objects.add(getAdditionalParam(model, args, method));
    }

    private Object getAdditionalParam(InvokerModel invokerModel, Object[] args, Method method) {
        AdditionalParam additionalParam = invokerModel.getAdditionalParam();
        if (Objects.nonNull(additionalParam)) {
            return twtCommonAdapter.takeParam(additionalParam.getType(), invokerModel, args, method);
        }
        return null;
    }
}
