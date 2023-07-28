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
 * @see org.khasanof.core.executors.expression.functions
 * @since 05.07.2023 22:27
 */
@FunctionParameter(name = "value")
@FunctionParameter(name = "const")
public class ContainsFunction extends AbstractFunction implements EvalFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        return new EvaluationValue(parameterValues[0].getStringValue().contains(parameterValues[1].getStringValue()));
    }


    @Override
    public String getName() {
        return "CONTAINS";
    }
}