package org.khasanof.springbootstarterfluent.core.executors.expression;

import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.expression.functions
 * @since 01.08.2023 21:38
 */
public interface ExpressionVariables {

    Map<String, String> getMatchVariables(String expression, String value);

    boolean isExpression(String expression);

    String[] getExpression(String expression);

}
