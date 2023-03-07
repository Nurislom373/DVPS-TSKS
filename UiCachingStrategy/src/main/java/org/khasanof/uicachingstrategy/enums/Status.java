package org.khasanof.uicachingstrategy.enums;

import java.util.Arrays;
import java.util.Random;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 4:33 PM
 * <br/>
 * Package: org.khasanof.uicaching.enums
 */
public enum Status {
    SUCCESS,
    FAIL;

    public static Status findAny() {
        return Arrays.asList(values())
                .get(new Random().nextInt(values().length));
    }
}
