package org.khasanof;

import org.junit.jupiter.api.Test;
import org.khasanof.core.executors.matcher.GenericMatcher;
import org.khasanof.main.annotation.HandleMessage;
import org.reflections.Reflections;

import java.util.Set;

/**
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @see org.khasanof
 * @since 24.06.2023 0:05
 */
public class QuestMethodTest {
    @Test
    void test() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("org.khasanof");

        Set<Class<? extends GenericMatcher>> subTypesOf = reflections.getSubTypesOf(GenericMatcher.class);
        subTypesOf.forEach(System.out::println);

        GenericMatcher genericMatcher = subTypesOf.iterator().next().newInstance();
        if (genericMatcher.getType().equals(HandleMessage.class)) {
            System.out.println("genericMatcher = " + genericMatcher);
        }
        System.out.println("genericMatcher = " + genericMatcher);

    }



}
