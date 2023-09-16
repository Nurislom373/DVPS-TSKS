package org.khasanof.springbootstarterfluent.core.event;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.springbootstarterfluent.core.event.methodInvoke.MethodV1Event;
import org.khasanof.springbootstarterfluent.core.executors.execution.CommonExecutionAdapter;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.event
 * @since 8/13/2023 8:56 PM
 */
@Slf4j
@Component
public class MethodInvokerV1EventListener {

    private final CommonExecutionAdapter commonExecutionAdapter;

    public MethodInvokerV1EventListener(CommonExecutionAdapter commonExecutionAdapter) {
        this.commonExecutionAdapter = commonExecutionAdapter;
    }

    @Async
    @EventListener(value = MethodV1Event.class)
    public void onApplicationEvent(MethodV1Event methodV1Event) {
        log.info("event listen invoker name : {}", methodV1Event.getInvokerModel().getName());
        commonExecutionAdapter.execution(methodV1Event);
    }

}
