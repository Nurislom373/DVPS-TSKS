package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.springbootstarterfluent.core.executors.expression.VariableExpressionMatcher;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.model.additional.checks.ACInvokerModel;
import org.khasanof.springbootstarterfluent.core.model.additional.param.APAnnotationMap;
import org.khasanof.springbootstarterfluent.core.model.additional.param.APUpdateObject;
import org.khasanof.springbootstarterfluent.core.utils.UpdateUtils;
import org.khasanof.springbootstarterfluent.main.FluentBot;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessFile;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.invoker
 * @since 8/11/2023 9:43 PM
 */
@Component
public class DefaultInvokerFunctions implements ApplicationContextAware {

    private final InvokerFunctions functions;
    private final DefaultInvokerMatcher invokerMatcher;
    private final VariableExpressionMatcher matcher = new VariableExpressionMatcher();

    public static final String EXCEPTION_NAME = "handleException";
    public static final String HANDLE_UPDATE = "handleUpdate";
    public static final String HANDLE_UPDATE_W_PROCESS_FL = "handleUpdateWithProcessFile";
    public static final String HANDLE_UPDATE_W_VAR_EXPRESSION = "handleUpdateWithVariableExpression";
    public static final String HANDLE_ANY_UPDATE = "handleAnyUpdate";
    public static final String HANDLE_STATE = "handleState"; // currently not is use!

    public DefaultInvokerFunctions(InvokerFunctions functions, DefaultInvokerMatcher invokerMatcher) {
        this.functions = functions;
        this.invokerMatcher = invokerMatcher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO this class is one of the state classes
        /*List<Class<?>> classList1 = List.of(Update.class, AbsSender.class, State.class);
        InvokerModel invokerModel3 = new InvokerModel(HANDLE_STATE, false, HandleState.class,
                classList1, true, new InvokerModel.MainParam(
                (update -> stateRepository.getState(UpdateUtils.getUserId(update), stateService.getType()))));
        addInvokerModel(invokerModel3);*/

        List<Class<?>> classList = List.of(Update.class, AbsSender.class);
        InvokerModel handleAnyModel = new InvokerModel(HANDLE_ANY_UPDATE, false, HandleAny.class,
                classList, false, null, true);
        functions.addInvokerModel(handleAnyModel);

        List<Class<?>> classList1 = List.of(Update.class, AbsSender.class, Map.class);
        InvokerModel invokerModel1 = new InvokerModel(HANDLE_UPDATE_W_VAR_EXPRESSION, false, HandleMessage.class,
                classList1, true, ((APAnnotationMap) (entry -> matcher.getMatchVariables(((HandleMessage)
                entry.getValue()).value(), entry.getKey().getMessage().getText()))),
                (ACInvokerModel) invokerMatcher::messageScopeEq);
        functions.addInvokerModel(invokerModel1);

        List<Class<?>> classList3 = List.of(Update.class, AbsSender.class, Throwable.class);
        InvokerModel invokerModel3 = new InvokerModel(EXCEPTION_NAME, false, HandleException.class,
                classList3, true, true);
        functions.addInvokerModel(invokerModel3);

        List<Class<?>> classList4 = List.of(Update.class, AbsSender.class, InputStream.class);
        InvokerModel invokerModel4 = new InvokerModel(HANDLE_UPDATE_W_PROCESS_FL, true, ProcessFile.class,
                classList4, true, ((APUpdateObject) (update ->
                UpdateUtils.getInputStreamWithFileId(UpdateUtils.getFileId(update),
                        applicationContext.getBean(FluentBot.class)))));
        functions.addInvokerModel(invokerModel4);

        List<Class<?>> classList2 = List.of(Update.class, AbsSender.class);
        InvokerModel invokerModel2 = new InvokerModel(HANDLE_UPDATE, true, ProcessUpdate.class,
                classList2, false, null);
        functions.addInvokerModel(invokerModel2);
    }
}
