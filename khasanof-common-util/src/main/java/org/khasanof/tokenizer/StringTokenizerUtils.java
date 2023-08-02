package org.khasanof.tokenizer;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.tokenizer
 * @since 01.08.2023 22:44
 */
public abstract class StringTokenizerUtils {

    public static List<String> getTokenWithList(String str, String delim) {
        return getTokensWithGeneric(str, delim, false, ArrayList::new);
    }

    public static List<String> getTokensWithList(String str, String delim, boolean returnDelim) {
        return getTokensWithGeneric(str, delim, returnDelim, ArrayList::new);
    }

    @SuppressWarnings("unchecked")
    public static  <T extends Collection> T getTokensWithGeneric(String str, String delim, boolean returnDelim,
                                                                Supplier<T> collectionFactory) {
        T objects = collectionFactory.get();
        StringTokenizer token = new StringTokenizer(str, delim, returnDelim);
        while (token.hasMoreElements()) {
            objects.add(token.nextToken());
        }
        return objects;
    }

}
