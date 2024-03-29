package org.khasanof.springbootstarterfluent.core.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.config
 * @since 8/4/2023 7:20 AM
 */
@Data
@ConfigurationProperties(prefix = "fluent")
public class ApplicationProperties {

    @Getter
    @Setter
    private Bot bot = new Bot();

    @Data
    public static class Bot {
        private String token;
        private String username;
        private ProcessType processType = ProcessType.UPDATE;
    }

}
