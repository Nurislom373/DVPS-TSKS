package org.khasanof.core.utils;

import lombok.SneakyThrows;
import org.khasanof.main.FluentStarter;

import javax.management.*;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Nurislom
 * @see org.khasanof.core.utils
 * @since 25.07.2023 20:55
 */
public class BaseUtils {

    public static void checkArgsIsNonNull(Object... args) {
        var anyMatch = Arrays.stream(args).anyMatch(Objects::isNull);
        if (anyMatch) {
            throw new NullPointerException("one or more param is null!");
        }
    }

    public static void main(String[] args) {
        BaseUtils baseUtils = new BaseUtils();
        String version = baseUtils.getVersion();
        System.out.println("version = " + version);
    }

    public synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = getClass().getResourceAsStream("/META-INF/maven/org.khasanof/my-artefact/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("artifactId", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = getClass().getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        if (version == null) {
            // we could not compute the version so use a blank
            version = "";
        }

        return version;
    }
}