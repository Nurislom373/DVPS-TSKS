package org.khasanof.springbootstarterfluent.core.executors.invoker;

import org.khasanof.condition.Condition;
import org.khasanof.springbootstarterfluent.core.enums.InvokerType;
import org.khasanof.springbootstarterfluent.core.executors.expression.VariableExpressionMatcher;
import org.khasanof.springbootstarterfluent.core.model.InvokerMethod;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;
import org.khasanof.springbootstarterfluent.core.model.InvokerModelV2;
import org.khasanof.springbootstarterfluent.core.model.additional.checks.ACInvokerMethod;
import org.khasanof.springbootstarterfluent.core.model.additional.checks.ACInvokerModel;
import org.khasanof.springbootstarterfluent.core.model.additional.param.APAnnotationMap;
import org.khasanof.springbootstarterfluent.core.model.additional.param.APUpdateObject;
import org.khasanof.springbootstarterfluent.core.model.additional.param.APUpdateState;
import org.khasanof.springbootstarterfluent.core.model.conditions.ClassCondition;
import org.khasanof.springbootstarterfluent.core.model.conditions.MethodCondition;
import org.khasanof.springbootstarterfluent.core.state.StateAction;
import org.khasanof.springbootstarterfluent.core.state.repository.StateRepositoryStrategy;
import org.khasanof.springbootstarterfluent.core.utils.AnnotationUtils;
import org.khasanof.springbootstarterfluent.core.utils.UpdateUtils;
import org.khasanof.springbootstarterfluent.main.FluentBot;
import org.khasanof.springbootstarterfluent.main.annotation.exception.HandleException;
import org.khasanof.springbootstarterfluent.main.annotation.extra.HandleState;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleAny;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleMessage;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessFile;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;
import org.khasanof.springbootstarterfluent.main.inferaces.state.State;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
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
public class DefaultInvokerFunctions implements InitializingBean {

    private final InvokerFunctions functions;
    private final StateRepositoryStrategy stateRepository;
    private final DefaultInvokerMatcher invokerMatcher;
    private final ApplicationContext applicationContext;
    private final VariableExpressionMatcher matcher = new VariableExpressionMatcher();

    public static final String EXCEPTION_NAME = "handleException";
    public static final String HANDLE_UPDATE = "handleUpdate";
    public static final String HANDLE_UPDATE_W_PROCESS_FL = "handleUpdateWithProcessFile";
    public static final String HANDLE_UPDATE_W_VAR_EXPRESSION = "handleUpdateWithVariableExpression";
    public static final String HANDLE_ANY_UPDATE = "handleAnyUpdate";
    public static final String HANDLE_STATE = "handleState";

    public DefaultInvokerFunctions(InvokerFunctions functions, StateRepositoryStrategy stateRepository, DefaultInvokerMatcher invokerMatcher, ApplicationContext applicationContext) {
        this.functions = functions;
        this.stateRepository = stateRepository;
        this.invokerMatcher = invokerMatcher;
        this.applicationContext = applicationContext;
    }

//    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<Class<?>> classListState = List.of(Update.class, AbsSender.class, State.class);
        InvokerModel invokerModel3 = new InvokerModel(HANDLE_STATE, false, HandleState.class,
                classListState, true, (APUpdateObject)
                (update -> stateRepository.findById(UpdateUtils.getUserId(update))
                        .orElseThrow(() -> new RuntimeException("State not found by userId!"))));
        functions.addInvokerModel(invokerModel3);

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
        InvokerModel invokerModelHandleExp = new InvokerModel(EXCEPTION_NAME, false, HandleException.class,
                classList3, true, true);
        functions.addInvokerModel(invokerModelHandleExp);

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

    @Override
    public void afterPropertiesSet() {
        InvokerModelV2 stateInvokerModel = InvokerModelV2.builder()
                .name(HANDLE_STATE)
                .type(InvokerType.CLASS)
                .condition((ClassCondition) (invokerClass -> StateAction.class.isAssignableFrom(
                        invokerClass.getReference().getClass())))
                .additionalParam((APUpdateState)
                        (update -> stateRepository.findById(UpdateUtils.getUserId(update))
                                .orElseThrow(() -> new RuntimeException("State not found by userId!"))))
                .methodParams(List.of(Update.class, AbsSender.class, State.class))
                .canBeNoParam(false)
                .isInputSystem(false)
                .build();
        functions.addInvokerModelV2(stateInvokerModel);

        InvokerModelV2 handleAny = InvokerModelV2.builder()
                .name(HANDLE_ANY_UPDATE)
                .type(InvokerType.METHOD)
                .condition((MethodCondition) (invokerMethod -> AnnotationUtils.hasAnnotation(invokerMethod.getMethod(),
                        HandleAny.class, false)))
                .methodParams(List.of(Update.class, AbsSender.class))
                .isInputSystem(false)
                .canBeNoParam(true)
                .build();
        functions.addInvokerModelV2(handleAny);

        InvokerModelV2 handleMessageScopeVarExpression = InvokerModelV2.builder()
                .name(HANDLE_UPDATE_W_VAR_EXPRESSION)
                .type(InvokerType.METHOD)
                .condition((MethodCondition) (invokerMethod -> AnnotationUtils.hasAnnotation(invokerMethod.getMethod(),
                        HandleMessage.class, false)))
                .additionalParam((APAnnotationMap) (entry -> matcher.getMatchVariables(((HandleMessage)
                        entry.getValue()).value(), entry.getKey().getMessage().getText())))
                .additionalChecks((ACInvokerMethod) invokerMatcher::messageScopeEq)
                .methodParams(List.of(Update.class, AbsSender.class, Map.class))
                .isInputSystem(false)
                .canBeNoParam(false)
                .build();
        functions.addInvokerModelV2(handleMessageScopeVarExpression);

        InvokerModelV2 handleException = InvokerModelV2.builder()
                .name(EXCEPTION_NAME)
                .type(InvokerType.METHOD)
                .condition((MethodCondition) (invokerMethod -> AnnotationUtils.hasAnnotation(invokerMethod.getMethod(),
                        HandleException.class, false)))
                .methodParams(List.of(Update.class, AbsSender.class, Throwable.class))
                .isInputSystem(true)
                .canBeNoParam(false)
                .build();
        functions.addInvokerModelV2(handleException);

        InvokerModelV2 handleProcessFile = InvokerModelV2.builder()
                .name(HANDLE_UPDATE_W_PROCESS_FL)
                .type(InvokerType.METHOD)
                .condition((MethodCondition) (this::handleUpdateWithProcessFileMethodCondition))
                .additionalParam((APUpdateObject) (update ->
                        UpdateUtils.getInputStreamWithFileId(UpdateUtils.getFileId(update),
                                applicationContext.getBean(FluentBot.class))))
                .methodParams(List.of(Update.class, AbsSender.class, InputStream.class))
                .isInputSystem(false)
                .canBeNoParam(false)
                .build();
        functions.addInvokerModelV2(handleProcessFile);

        InvokerModelV2 handleUpdates = InvokerModelV2.builder()
                .name(HANDLE_UPDATE)
                .type(InvokerType.METHOD)
                .condition((MethodCondition) (invokerMethod -> AnnotationUtils.hasAnnotation(invokerMethod.getMethod(),
                        ProcessUpdate.class, true)))
                .methodParams(List.of(Update.class, AbsSender.class))
                .isInputSystem(false)
                .canBeNoParam(false)
                .build();
        functions.addInvokerModelV2(handleUpdates);

    }

    private boolean handleUpdateWithProcessFileMethodCondition(InvokerMethod invokerMethod) {
        return Condition.orElse(() -> {
            return AnnotationUtils.hasAnnotation(invokerMethod.getMethod(), ProcessFile.class, true) &&
                    (invokerMethod.getMethod().getParameterCount() > 2);
        }, true, false);

    }

}
