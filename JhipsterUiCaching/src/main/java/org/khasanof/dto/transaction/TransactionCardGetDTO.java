package org.khasanof.dto.transaction;

import lombok.Getter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/16/2023
 * <br/>
 * Time: 10:35 PM
 * <br/>
 * Package: org.khasanof.dto.transaction
 */
@ParameterObject
public class TransactionCardGetDTO {

    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    public TransactionCardGetDTO(String cardNumber, LocalDateTime from, LocalDateTime to) {
        this.cardNumber = cardNumber;
        this.from = from;
        this.to = to;
    }

    public TransactionCardGetDTO() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionCardGetDTO that = (TransactionCardGetDTO) o;

        if (!Objects.equals(cardNumber, that.cardNumber)) return false;
        if (!Objects.equals(from, that.from)) return false;
        return Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        int result = cardNumber != null ? cardNumber.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransactionCardGetDTO{" +
            "cardNumber='" + cardNumber + '\'' +
            ", from=" + from +
            ", to=" + to +
            '}';
    }
}
