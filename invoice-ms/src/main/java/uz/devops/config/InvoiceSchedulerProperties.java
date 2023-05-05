package uz.devops.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: Nurislom
 * <br/>
 * Date: 05.05.2023
 * <br/>
 * Time: 17:49
 * <br/>
 * Package: uz.devops.config
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "invoice.scheduler")
public class InvoiceSchedulerProperties {
    public static String CRONJOB;
}
