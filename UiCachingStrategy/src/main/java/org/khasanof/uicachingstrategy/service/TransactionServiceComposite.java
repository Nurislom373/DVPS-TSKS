package org.khasanof.uicachingstrategy.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.khasanof.uicachingstrategy.annotation.TransactionType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/9/2023
 * <br/>
 * Time: 9:18 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.service
 */
@Service
@NoArgsConstructor
public class TransactionServiceComposite implements InitializingBean {

    private List<TransactionService> services = new ArrayList<>();

    @Autowired
    private ContextTransactionServices contextTransactionServices;

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        services.addAll(contextTransactionServices.getServices());
    }
}
