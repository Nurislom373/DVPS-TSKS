package org.khasanof.springbootstarterfluent.core.executors;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.custom.BreakerForEach;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.executors.invoker.Invoker;
import org.khasanof.springbootstarterfluent.core.executors.invoker.InvokerFunctions;
import org.khasanof.springbootstarterfluent.main.FluentBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @version 1.0.5
 * @see org.khasanof.core.executors
 * @since 24.06.2023 0:46
 */
@Component
public class CommonUpdateExecutor extends AbstractExecutor {

    private final InvokerFunctions invokerFunctions; // specifies the methods that can be invoked.
    private final DeterminationUpdate determination; // gathers the methods corresponding to the incoming update.
    private final Invoker invoker; // method invoker
    private final FluentBot fluentBot; // bot instance

    public CommonUpdateExecutor(InvokerFunctions invokerFunctions, DeterminationUpdate determinationUpdateType, Invoker invoker, @Lazy FluentBot fluentBot, Collector collector) {
        super(collector);
        this.invokerFunctions = invokerFunctions;
        this.determination = determinationUpdateType;
        this.invoker = invoker;
        this.fluentBot = fluentBot;
    }

    /**
     * Using the {@link DeterminationUpdate#determinationV2(Update)} method, update collects the matching methods.
     * iterates the collected methods one by one. If an error or something happens, the execution of methods is stopped.
     *
     * @param update from telegram is coming.
     */
    public void executeV2(Update update) {
        BreakerForEach.forEach(determination.determinationV2(update).entrySet().stream(),
                ((entry, breaker) -> {
                    if (!FluentContext.updateExecutorBoolean.get()) {
                        invoker.invoke(invokerFunctions.fillAndGet(entry, update, fluentBot));
                    } else {
                        breaker.stop();
                    }
                }), () -> FluentContext.updateExecutorBoolean.set(false));
    }
}
