package org.khasanof.springbootstarterfluent.core.model;

import lombok.*;
import org.khasanof.springbootstarterfluent.core.enums.InvokerType;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/19/2023 6:16 PM
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvokerObject implements InvokerResult {

    private Object reference;
    private String executionMethodName;

    @Override
    public InvokerType getType() {
        return InvokerType.CLASS;
    }
}
