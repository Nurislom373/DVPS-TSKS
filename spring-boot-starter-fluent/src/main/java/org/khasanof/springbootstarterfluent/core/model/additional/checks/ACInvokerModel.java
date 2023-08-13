package org.khasanof.springbootstarterfluent.core.model.additional.checks;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.model.AdditionalChecks;
import org.khasanof.springbootstarterfluent.core.model.InvokerModel;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model.additional.checks
 * @since 8/13/2023 6:49 PM
 */
public interface ACInvokerModel extends AdditionalChecks<InvokerModel> {

    @Override
    default AdditionalParamType getType() {
        return AdditionalParamType.VAR_EXPRESSION_PARAM;
    }

}
