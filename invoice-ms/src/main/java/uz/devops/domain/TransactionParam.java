package uz.devops.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.devops.domain.enumeration.Status;

/**
 * TransactionParam
 */
@Entity
@Table(name = "transaction_param")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * TransactionParam status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    /**
     * Transaction id on bm-db
     */
    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> params;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public TransactionParam(Status status, Long transactionId, Map<String, String> params) {
        this.status = status;
        this.transactionId = transactionId;
        this.params = params;
    }

    public TransactionParam() {
    }

    public Long getId() {
        return this.id;
    }

    public TransactionParam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public TransactionParam status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public TransactionParam transactionId(Long transactionId) {
        this.setTransactionId(transactionId);
        return this;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionParam transactionParam = (TransactionParam) o;
        return Objects.equals(id, transactionParam.id) && status == transactionParam.status && Objects.equals(transactionId, transactionParam.transactionId) && Objects.equals(params, transactionParam.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, transactionId, params);
    }

    @Override
    public String toString() {
        return "TransactionParam{" +
            "id=" + id +
            ", status=" + status +
            ", transactionId=" + transactionId +
            ", params=" + params +
            '}';
    }
}
