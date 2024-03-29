package org.khasanof.springbootstarterfluent.core.model.additional.param;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.model.AdditionalParam;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model.additional
 * @since 8/10/2023 8:05 AM
 */
public interface APUpdateObject extends AdditionalParam<Update, Object> {

    @Override
    default Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    default AdditionalParamType getType() {
        return AdditionalParamType.PROCESS_FILE_PARAM;
    }

}
