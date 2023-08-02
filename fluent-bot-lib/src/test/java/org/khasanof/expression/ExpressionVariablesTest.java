package org.khasanof.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.core.executors.expression.CommonVariableMatcher;
import org.khasanof.core.executors.expression.ExpressionVariables;
import org.khasanof.core.executors.expression.VariableExpressionMatcher;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author Nurislom
 * @see org.khasanof.expression
 * @since 01.08.2023 22:20
 */
public class ExpressionVariablesTest {

    ExpressionVariables expressionVariables = new VariableExpressionMatcher();
    CommonVariableMatcher matcher = new CommonVariableMatcher();

    @Test
    void test() {
        StringTokenizer stringTokenizer = new StringTokenizer("/hello {param:[0-9]} {name:[\\\\S]+}", "{.}", false);
        stringTokenizer.asIterator().forEachRemaining(System.out::println);
        String[] expression = expressionVariables.getExpression("/hello {param:[0-9]} {name:[\\\\S]+}");
        boolean expression1 = expressionVariables.isExpression("param:[0-9]");
        boolean match = matcher.match("/hello {param:[0-9]} {name:[\\\\S]+}", "/hello 4242542 Nurislom");
        System.out.println("match = " + match);
        Pattern compile = Pattern.compile(".");
        System.out.println("compile = " + compile);
        Assertions.assertEquals(expression.length, 2);
    }

}
