package uz.devops.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Properties specific to Invoicems.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    // jhipster-needle-application-properties-property
    // jhipster-needle-application-properties-property-getter
    // jhipster-needle-application-properties-property-class

    public static final String MONTHLY_INVOICE_SCHEDULE_CRONE_PATTERN = "0 30 0 1 * ?";

    private static final String INN = "inn";
    private static final String COMPANY_NAME = "company_name";
    private static final String ADDRESS_REGION = "address_region";
    private static final String ADDRESS_STREET = "address_street";
    private static final String BANK_DETAILS_ACC_NUM = "bank_details_acc_num";
    private static final String BANK_DETAILS_BANK_NAME = "bank_details_bank_name";
    private static final String BANK_DETAILS_BANK_CODE = "bank_details_bank_code";

    public static final String SENDER_INFO = "sender_info";
    public static final String SENDER_INFO_INN = SENDER_INFO + "_" + INN;
    public static final String SENDER_INFO_COMPANY_NAME = SENDER_INFO + "_" + COMPANY_NAME;
    public static final String SENDER_INFO_ADDRESS_REGION = SENDER_INFO + "_" + ADDRESS_REGION;
    public static final String SENDER_INFO_ADDRESS_STREET = SENDER_INFO + "_" + ADDRESS_STREET;
    public static final String SENDER_INFO_BANK_DETAILS_ACC_NUM = SENDER_INFO + "_" + BANK_DETAILS_ACC_NUM;
    public static final String SENDER_INFO_BANK_DETAILS_BANK_NAME = SENDER_INFO + "_" + BANK_DETAILS_BANK_NAME;
    public static final String SENDER_INFO_BANK_DETAILS_BANK_CODE = SENDER_INFO + "_" + BANK_DETAILS_BANK_CODE;

    public static final String RECEIVER_INFO = "receiver_info";
    public static final String RECEIVER_INFO_INN = RECEIVER_INFO + "_" + INN;
    public static final String RECEIVER_INFO_COMPANY_NAME = RECEIVER_INFO + "_" + COMPANY_NAME;
    public static final String RECEIVER_INFO_ADDRESS_REGION = RECEIVER_INFO + "_" + ADDRESS_REGION;
    public static final String RECEIVER_INFO_ADDRESS_STREET = RECEIVER_INFO + "_" + ADDRESS_STREET;
    public static final String RECEIVER_INFO_BANK_DETAILS_ACC_NUM = RECEIVER_INFO + "_" + BANK_DETAILS_ACC_NUM;
    public static final String RECEIVER_INFO_BANK_DETAILS_BANK_NAME = RECEIVER_INFO + "_" + BANK_DETAILS_BANK_NAME;
    public static final String RECEIVER_INFO_BANK_DETAILS_BANK_CODE = RECEIVER_INFO + "_" + BANK_DETAILS_BANK_CODE;

    public static final List<String> JSON_REQUIRED_FIELDS = List.of(
        SENDER_INFO_INN, SENDER_INFO_COMPANY_NAME, SENDER_INFO_ADDRESS_REGION, SENDER_INFO_ADDRESS_STREET,
        RECEIVER_INFO_INN, RECEIVER_INFO_COMPANY_NAME, RECEIVER_INFO_ADDRESS_REGION, RECEIVER_INFO_ADDRESS_STREET
    );

}
