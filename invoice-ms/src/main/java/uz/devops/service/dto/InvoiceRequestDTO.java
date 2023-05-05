package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import uz.devops.domain.enumeration.Status;

/**
 * A DTO for the {@link uz.devops.domain.InvoiceRequest} entity.
 */
@Schema(description = "Запрос счета-фактуры")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceRequestDTO implements Serializable {

    private Long id;

    /**
     * Статус запроса счёт-фактуры
     */
    @Schema(description = "Статус запроса счёт-фактуры")
    private Status status;

    /**
     * Запрос в json формате
     */

    @Schema(description = "Запрос в json формате", required = true)
    @Lob
    private String requestJson;

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

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceRequestDTO)) {
            return false;
        }

        InvoiceRequestDTO invoiceRequestDTO = (InvoiceRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceRequestDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", requestJson='" + getRequestJson() + "'" +
            "}";
    }
}
