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
    private final ApplicationConfigContext context = ApplicationConfigContext.getConfigInstance();

    @Override
    public Configs getConfig() {
        return getConfig(settings);
    }

    @Override
    public void start() {
        Set<Class<? extends Config>> classes = reflections.getSubTypesOf(Config.class);
        classes.stream().filter(MethodUtils::checkInstance).map(clazz -> {
                    try {
                        return clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).peek(Config::runnable)
                .forEach(obj -> context.add(obj.getClass(), obj));
    }

    private Configs getConfig(ResourceBundle settings) {
        return Configs.builder()
                .token(settings.getString("bot.token"))
                .username(settings.getString("bot.username"))
                .processType(ProcessType.valueOf(settings.getString("bot.process.type")))
                .projectArtifactId(settings.getString("bot.project.artifactId"))
                .projectGroupId(settings.getString("bot.project.groupId"))
                .build();
    }

    private Predicate<Config> createPredicate(Configs configs) {
        return (config -> {
            if (configs.getProcessType().equals(ProcessType.BOTH)) {
                return true;
            }
            return config.processType().equals(configs.getProcessType());
        });
    }
}
