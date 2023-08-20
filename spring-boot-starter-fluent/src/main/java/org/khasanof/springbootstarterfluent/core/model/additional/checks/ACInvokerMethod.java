package org.khasanof.springbootstarterfluent.core.model.additional.checks;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.model.AdditionalChecks;
import org.khasanof.springbootstarterfluent.core.model.InvokerMethod;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model.additional.checks
 * @since 8/20/2023 3:27 PM
 */
public interface ACInvokerMethod extends AdditionalChecks<InvokerMethod> {

    @Override
    default AdditionalParamType getType() {
        return AdditionalParamType.VAR_EXPRESSION_PARAM;
    }

}
