package org.khasanof.reactiveuicaching.utils;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 7:07 PM
 * <br/>
 * Package: org.khasanof.uicaching.utils
 */
public abstract class BaseUtils {

    public static LocalDateTime getRandomLocalDateToBetween(int from, int to) {
        return LocalDateTime.now().minusDays(new Random().nextLong(from, to));
    }


}
