package org.khasanof.core.executors.expression;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.ParseException;
import org.khasanof.core.executors.expression.functions.ContainsFunction;
import org.khasanof.core.executors.expression.functions.EndWithFunction;
import org.khasanof.core.executors.expression.functions.EqualsFunction;
import org.khasanof.core.executors.expression.functions.StartWithFunction;

import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.expression
 * @since 02.07.2023 19:18
 */
public class CommonExpression {

    private final ExpressionConfiguration configuration = ExpressionConfiguration.defaultConfiguration()
            .withAdditionalFunctions(
                    Map.entry("END_WITH", new EndWithFunction()),
                    Map.entry("START_WITH", new StartWithFunction()),
                    Map.entry("EQUALS", new EqualsFunction()),
                    Map.entry("CONTAINS", new ContainsFunction())
            );

    public boolean isMatch(String expression, Object updVal) {
        try {
            System.out.println("expression = " + expression);
            String replaced = expression.replaceAll("'", "\"");
            System.out.println("replaced = " + replaced);
            System.out.println("updVal = " + updVal);
            boolean value = new Expression(replaced, configuration)
                    .and("value", updVal).evaluate()
                    .getBooleanValue();
            System.out.println("value = " + value);
            return value;
        } catch (EvaluationException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
