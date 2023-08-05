package org.khasanof.springbootstarterfluent.core.config;

import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.khasanof.springbootstarterfluent.core.utils.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 05.07.2023 0:29
 */
public class CommonFluentConfig implements FluentConfig, ApplicationContextAware {

    private final Reflections reflections = ReflectionUtils.getReflections(true);
    public final ResourceBundle settings = ResourceBundle.getBundle("application");
    private final ApplicationConfigContext context = ApplicationConfigContext.getConfigInstance();

    @Override
    public Configs getConfig() {
        return getConfig(settings);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Config> beans = applicationContext.getBeansOfType(Config.class);
        beans.values().forEach(Config::runnable);
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
