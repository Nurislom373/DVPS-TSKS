package org.khasanof.springbootstarterfluent.core.model;

import lombok.*;
import org.khasanof.springbootstarterfluent.core.enums.InvokerType;

import java.util.List;
import java.util.Objects;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/19/2023 6:31 PM
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvokerModelV2 {

    private InvokerType type;
    private String name;
    private InvokerResult invokerReference;
    private Condition condition;
    private AdditionalParam additionalParam;
    private AdditionalChecks additionalChecks;
    private List<Class<?>> methodParams;
    private Object[] args;
    private boolean isInputSystem;
    private boolean canBeNoParam;

    public boolean hasAdditionalParam() {
        return Objects.nonNull(this.additionalParam);
    }

    public boolean hasAdditionalChecks() {
        return Objects.nonNull(this.additionalChecks);
    }

    public boolean hasReference() {
        return Objects.nonNull(invokerReference);
    }

}
