package org.khasanof.core.config;

import org.khasanof.core.enums.ProcessType;
import org.khasanof.core.utils.MethodUtils;
import org.khasanof.core.utils.ReflectionUtils;
import org.reflections.Reflections;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 05.07.2023 0:29
 */
public class CommonFluentConfig implements FluentConfig {

    private final Reflections reflections = ReflectionUtils.getReflections(true);
    public final ResourceBundle settings = ResourceBundle.getBundle("application");

    @Override
    public Configs getConfig() {
        return getConfig(settings);
    }

    @Override
    public void start() {
        Configs configs = getConfig(settings);
        Set<Class<? extends Config>> classes = reflections.getSubTypesOf(Config.class);
        classes.stream().filter(MethodUtils::checkInstance).map(clazz -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).forEach(Config::runnable);
    }

    private Configs getConfig(ResourceBundle settings) {
        return Configs.builder()
                .token(settings.getString("bot.token"))
                .username(settings.getString("bot.username"))
                .processType(ProcessType.valueOf(settings.getString("bot.process.type")))
                .build();
    }

    private Predicate<Config> createPredicate(Configs configs) {
        return (config -> {
            if (config.processType().equals(ProcessType.BOTH)) {
                return true;
            }
            return config.processType().equals(configs.getProcessType());
        });
    }
}
