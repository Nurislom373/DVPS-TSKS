package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.*;

import uz.devops.domain.TransactionParam;
import uz.devops.domain.enumeration.Status;

/**
 * A DTO for the {@link TransactionParam} entity.
 */
@Schema(description = "Transaction param")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionParamDTO implements Serializable {

    private Long id;

    /**
     * TransactionParam status
     */
    @Schema(description = "Transaction param status")
    private Status status;

    /**
     * Transaction id on bm-db
     */
    @NotNull
    @Schema(description = "Transaction id on bm-db", required = true)
    private Long transactionId;

    private Map<String, String> params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionParamDTO that = (TransactionParamDTO) o;
        return Objects.equals(id, that.id) && status == that.status && Objects.equals(transactionId, that.transactionId)
            && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, transactionId, params);
    }

    @Override
    public String toString() {
        return "TransactionParamDTO{" +
            "id=" + id +
            ", status=" + status +
            ", transactionId=" + transactionId +
            ", params=" + params +
            '}';
    }
}
