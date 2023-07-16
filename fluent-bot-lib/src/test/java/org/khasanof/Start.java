package org.khasanof;

import org.khasanof.main.inferaces.state.StateConfiguration;
import org.khasanof.main.FluentStarter;
import org.khasanof.main.annotation.HandlerScanner;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 14:18
 * <br/>
 * Package: org.khasanof
 */
@HandlerScanner(value = "org.khasanof")
public class Start implements StateConfiguration {

    public static void main(String[] args) {
        FluentStarter.start();
    }

    @Override
    public List<String> states() {
        return List.of("START", "LOG");
    }
}