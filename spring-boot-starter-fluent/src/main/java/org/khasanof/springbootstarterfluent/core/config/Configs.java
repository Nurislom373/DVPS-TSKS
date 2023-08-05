package org.khasanof.springbootstarterfluent.core.config;

import lombok.Builder;
import lombok.Getter;
import org.khasanof.springbootstarterfluent.core.enums.ProcessType;

/**
 * @author Nurislom
 * @see org.khasanof.core.config
 * @since 22.07.2023 11:22
 */
@Getter
@Builder
public class Configs {
    private String token;
    private String username;
    private ProcessType processType;
    private String projectArtifactId;
    private String projectGroupId;
}
