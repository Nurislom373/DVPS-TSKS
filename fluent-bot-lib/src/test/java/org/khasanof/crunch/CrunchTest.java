package org.khasanof.crunch;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nurislom
 * @see org.khasanof.crunch
 * @since 02.07.2023 19:02
 */
public class CrunchTest {

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
