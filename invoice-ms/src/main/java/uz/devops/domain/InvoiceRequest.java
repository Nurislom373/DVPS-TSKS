package uz.devops.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import uz.devops.domain.enumeration.Status;

/**
 * Запрос счета-фактуры
 */
@Entity
@Table(name = "invoice_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Статус запроса счёт-фактуры
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    /**
     * Запрос в json формате
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "request_json", nullable = false)
    private String requestJson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public InvoiceRequest status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRequestJson() {
        return this.requestJson;
    }

    public InvoiceRequest requestJson(String requestJson) {
        this.setRequestJson(requestJson);
        return this;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceRequest)) {
            return false;
        }
        return id != null && id.equals(((InvoiceRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceRequest{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", requestJson='" + getRequestJson() + "'" +
            "}";
    }
}
