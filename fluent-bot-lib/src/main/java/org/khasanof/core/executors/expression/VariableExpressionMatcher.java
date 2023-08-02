package org.khasanof.core.executors.expression;

import org.khasanof.tokenizer.StringTokenizerUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.expression
 * @since 30.07.2023 20:59
 */
public class VariableExpressionMatcher implements ExpressionMatcher<String>, ExpressionVariables {

    private final CommonVariableMatcher matcher = new CommonVariableMatcher();

    @Override
    public boolean doMatch(String expression, String value) {
        return matcher.match(expression, value);
    }

    @Override
    public boolean doMatch(String expression) {
        return matcher.isPattern(expression);
    }

    @Override
    public Map<String, String> getMatchVariables(String expression, String value) {
        return matcher.extractUriTemplateVariables(expression, value);
    }

    @Override
    public boolean isExpression(String expression) {
        List<String> list = StringTokenizerUtils.getTokenWithList(expression, "{.}");
        List<String> strings = list.stream().filter(f -> f.contains(":") &&
                isValidRegex(getRegex(f))).toList();
        return strings.isEmpty();
    }

    @Override
    public String[] getExpression(String expression) {
        return matcher.tokenizePattern(expression);
    }

    private String getRegex(String var) {
        return var.substring(var.indexOf(":"), var.length() - 1);
    }

    private boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

}
