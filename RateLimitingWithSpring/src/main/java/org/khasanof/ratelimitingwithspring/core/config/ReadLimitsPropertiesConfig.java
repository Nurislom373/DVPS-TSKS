package org.khasanof.ratelimitingwithspring.core.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.khasanof.ratelimitingwithspring.core.common.read.SaveDLLEnum;
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

    /*
        Secret Key, JWT Algorithm and etc...
     */
    @PostConstruct
    void afterPropertiesSet() {
        setSaveDLLEnum(environment.getProperty("api.limits.ddl", SaveDLLEnum.class));
        setPackageEnabled(environment.getProperty("api.packages.enabled", Boolean.class));
        setApiLimitsEnabled(environment.getProperty("api.limits.enabled", Boolean.class));
        setPackagesConfigFilePath(environment.getProperty("api.packages.file-path"));
        setApiLimitsConfigFilePath(environment.getProperty("api.limits.file-path"));
        setUserKey(environment.getProperty("api.user-key"));
    }

    // write logic ddl
    private SaveDLLEnum saveDLLEnum;
    private String packagesConfigFilePath;
    private String apiLimitsConfigFilePath;
    private Boolean packageEnabled;
    private Boolean apiLimitsEnabled;
    private String userKey;
}
