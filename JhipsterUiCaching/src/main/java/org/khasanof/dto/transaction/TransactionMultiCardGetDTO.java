package org.khasanof.dto.transaction;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/16/2023
 * <br/>
 * Time: 10:40 PM
 * <br/>
 * Package: org.khasanof.dto.transaction
 */
@ParameterObject
public class TransactionMultiCardGetDTO {

    @NotNull
    private List<String> cards;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    public TransactionMultiCardGetDTO(List<String> cards, LocalDateTime from, LocalDateTime to) {
        this.cards = cards;
        this.from = from;
        this.to = to;
    }

    public TransactionMultiCardGetDTO() {
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
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

        TransactionMultiCardGetDTO that = (TransactionMultiCardGetDTO) o;

        if (!Objects.equals(cards, that.cards)) return false;
        if (!Objects.equals(from, that.from)) return false;
        return Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        int result = cards != null ? cards.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TransactionMultiCardGetDTO{" +
            "cards=" + cards +
            ", from=" + from +
            ", to=" + to +
            '}';
    }
}
