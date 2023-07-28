package org.khasanof;

import org.khasanof.core.state.InitializingStateEnum;
import org.khasanof.main.inferaces.state.StateConfiguration;
import org.khasanof.main.FluentStarter;
import org.khasanof.main.annotation.HandlerScan;

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
@HandlerScan(value = "org.khasanof")
public class Start implements InitializingStateEnum {

    public static void main(String[] args) {
        FluentStarter.start();
    }

    @Override
    public Class<? extends Enum> getType() {
        return State.class;
    }
}