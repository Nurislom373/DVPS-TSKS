package org.khasanof.springbootstarterfluent.core.executors.invoker.additional.param.twts;

import org.khasanof.springbootstarterfluent.core.enums.additional.AdditionalParamType;
import org.khasanof.springbootstarterfluent.core.executors.invoker.additional.param.TWT;
import org.khasanof.springbootstarterfluent.core.model.InvokerModelV2;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.invoker.additional.param.twts
 * @since 8/20/2023 5:15 PM
 */
@Component
public class TWTStateParam implements TWT {

    @Override
    @SuppressWarnings("unchecked")
    public Object getValue(InvokerModelV2 invokerModel, Object[] args, Method method) {
        return invokerModel.getAdditionalParam().getParam(MethodUtils.getArg(args, Update.class));
    }

    @Override
    public AdditionalParamType getType() {
        return AdditionalParamType.STATE_PARAM;
    }
}
