package org.khasanof.ratelimitingwithspring.core.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 3:42 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public class PropertyConfig {

    private static Properties p;

    static {
        load();
    }

    private static void load() {
        try (FileReader fileReader = new FileReader("/application.properties")) {
            p = new Properties();
            p.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return p.getProperty(key);
    }
}
