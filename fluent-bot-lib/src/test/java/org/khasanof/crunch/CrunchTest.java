package org.khasanof.crunch;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nurislom
 * @see org.khasanof.crunch
 * @since 02.07.2023 19:02
 */
public class CrunchTest {

    @Test
    void crunchTest() {
        assertEquals(10, Crunch.evaluateExpression("$1 - $2", 20, 10));
        assertEquals(2, Crunch.evaluateExpression("$1 / $2", 20, 10));
        int someValue = 50;
        assertEquals(47, Crunch.evaluateExpression("$1 - 3", someValue));
    }

    @Test
    void evalTest() throws EvaluationException, ParseException {
        Expression expression = new Expression("(a + b) * (a - b)");

        EvaluationValue result = expression
                .with("a", 3.5)
                .and("b", 2.5)
                .evaluate();

        System.out.println(result.getNumberValue()); // prints 6.00
        assertEquals(result.getNumberValue(), new BigDecimal("6"));
    }

}
