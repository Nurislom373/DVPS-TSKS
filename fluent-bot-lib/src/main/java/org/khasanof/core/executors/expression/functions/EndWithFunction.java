package org.khasanof.core.executors.expression.functions;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import org.khasanof.core.executors.expression.EvalFunction;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.expression
 * @since 02.07.2023 19:23
 */
@FunctionParameter(name = "value")
@FunctionParameter(name = "const")
public class EndWithFunction extends AbstractFunction implements EvalFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... parameterValues) throws EvaluationException {
        boolean endsed = "ghfud".endsWith("s");
        return new EvaluationValue(parameterValues[1].getStringValue()
                .endsWith(parameterValues[0].getStringValue()));
    }

    @Override
    public String getName() {
        return "END_WITH";
    }
}
