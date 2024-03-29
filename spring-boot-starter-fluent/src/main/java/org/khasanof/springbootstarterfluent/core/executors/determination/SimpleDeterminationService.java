package org.khasanof.springbootstarterfluent.core.executors.determination;

import org.khasanof.springbootstarterfluent.core.config.ApplicationProperties;
import org.khasanof.springbootstarterfluent.core.config.Config;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.model.InvokerResult;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.determination
 * @since 16.07.2023 17:00
 */
@Component
public class SimpleDeterminationService implements DeterminationService, Config {

    private final Map<Integer, List<BiConsumer<Update, Set<InvokerResult>>>> orderListMap = new TreeMap<>();
    private final ApplicationContext applicationContext;
    private final ApplicationProperties.Bot bot;

    public SimpleDeterminationService(ApplicationContext applicationContext, ApplicationProperties properties) {
        this.applicationContext = applicationContext;
        this.bot = properties.getBot();
    }

    @Override
    public List<BiConsumer<Update, Set<InvokerResult>>> getDeterminations() {
        return orderListMap.values().stream().filter(Objects::nonNull)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public void runnable() {
        DeterminationAdapter determinationAdapter = new DeterminationAdapter();
        determinationAdapter.fillMap(orderListMap, applicationContext, this.bot.getProcessType());
    }

    @Override
    public ProcessType processType() {
        return ProcessType.BOTH;
    }
}
