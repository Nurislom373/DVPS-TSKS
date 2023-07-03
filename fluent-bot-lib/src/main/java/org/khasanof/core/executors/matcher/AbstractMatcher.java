package org.khasanof.core.executors.matcher;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.executors.expression.CommonExpression;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 03.07.2023 17:47
 */
public abstract class AbstractMatcher {

    protected final CommonExpression commonExpression = new CommonExpression();

    protected final Map<Map.Entry<MatchScope, Class>,
            BiFunction<Object, Object, Boolean>> matchFunctions = new HashMap<>();

    {
        setUp();
    }

    private void setUp() {
        matchFunctions.put(Map.entry(MatchScope.EQUALS, Integer.class), (var1, var2) -> (int) var1 == (int) var2);
        matchFunctions.put(Map.entry(MatchScope.EQUALS, String.class), (var1, var2) -> String.valueOf(var1)
                .equals(String.valueOf(var2)));
        matchFunctions.put(Map.entry(MatchScope.EQUALS_IGNORE_CASE, String.class), (var1, var2) -> String.valueOf(var1)
                .equalsIgnoreCase(String.valueOf(var2)));
        matchFunctions.put(Map.entry(MatchScope.END_WITH, String.class), (var1, var2) -> String.valueOf(var2)
                .endsWith(String.valueOf(var1)));
        matchFunctions.put(Map.entry(MatchScope.START_WITH, String.class), (var1, var2) -> String.valueOf(var2)
                .startsWith(String.valueOf(var1)));
        matchFunctions.put(Map.entry(MatchScope.CONTAINS, String.class), (var1, var2) -> String.valueOf(var2)
                .contains(String.valueOf(var1)));
        matchFunctions.put(Map.entry(MatchScope.REGEX, Object.class), (var1, var2) ->
                Pattern.compile(String.valueOf(var1)).matcher(String.valueOf(var2)).find());
        matchFunctions.put(Map.entry(MatchScope.EXPRESSION, Object.class), (var1, var2) ->
                commonExpression.isMatch(String.valueOf(var1), var2));
    }

    protected Class getScopeType(Object type, MatchScope scope) {
        if (scope.equals(MatchScope.EXPRESSION) || scope.equals(MatchScope.REGEX)) {
            return Object.class;
        }
        return type.getClass();
    }

}
