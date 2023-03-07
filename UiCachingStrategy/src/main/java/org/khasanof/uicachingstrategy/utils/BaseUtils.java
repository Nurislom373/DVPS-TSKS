package org.khasanof.uicachingstrategy.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public static boolean isValid(String var, DateTimeFormatter formatter) {
        try {
            formatter.parse(var);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }


}
