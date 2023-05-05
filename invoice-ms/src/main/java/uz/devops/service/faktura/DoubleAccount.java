package uz.devops.service.faktura;

import lombok.*;

import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 02.05.2023
 * <br/>
 * Time: 18:46
 * <br/>
 * Package: uz.devops.service.faktura
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DoubleAccount {

    private String senderAcc;

    private String receiverAcc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleAccount doubleAcc = (DoubleAccount) o;
        return Objects.equals(senderAcc, doubleAcc.senderAcc) && Objects.equals(receiverAcc, doubleAcc.receiverAcc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderAcc, receiverAcc);
    }
}
