package org.khasanof.ratelimitingwithspring.core.limiting;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 1:25 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.limiting
 */
@Getter
@Setter
@Component
public class ReadLimitsPropertiesConfig {

    @Autowired
    private Environment environment;

    @PostConstruct
    void afterPropertiesSet() {
        setPackageEnabled(environment.getProperty("api.packages.enabled", Boolean.class));
        setApiLimitsEnabled(environment.getProperty("api.limits.enabled", Boolean.class));
        setPackagesConfigFilePath(environment.getProperty("api.packages.file-path"));
        setApiLimitsConfigFilePath(environment.getProperty("api.limits.file-path"));
        setUserKey(environment.getProperty("api.user-key"));
    }

    private String packagesConfigFilePath;
    private String apiLimitsConfigFilePath;
    private Boolean packageEnabled;
    private Boolean apiLimitsEnabled;
    private String userKey;
}
