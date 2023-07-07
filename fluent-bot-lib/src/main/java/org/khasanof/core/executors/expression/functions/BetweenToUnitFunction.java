package org.khasanof.core.executors.expression.functions;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import org.khasanof.core.enums.MemoryUnits;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.expression.functions
 * @since 07.07.2023 23:28
 */
@FunctionParameter(name = "value")
@FunctionParameter(name = "begin")
@FunctionParameter(name = "end")
@FunctionParameter(name = "unit")
public class BetweenToUnitFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        MemoryUnits memoryUnits = MemoryUnits.valueOf(parameterValues[3].getStringValue());
        return null;
    }

}
