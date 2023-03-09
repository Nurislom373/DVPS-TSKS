package org.khasanof.uicachingstrategy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.khasanof.uicachingstrategy.service.humo.HumoTransactionService;
import org.khasanof.uicachingstrategy.service.uzcard.UzCardTransactionService;

import java.util.stream.Stream;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/9/2023
 * <br/>
 * Time: 12:27 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.enums
 */
@Getter
@RequiredArgsConstructor
public enum CardType {
    UZCARD("8600", UzCardTransactionService.class),
    HUMO("9860", HumoTransactionService.class);
    private final String cardNumber;
    private final Class<?> classType;

    public static Class<?> getClasWithCardNum(String cardNumber) {
        return Stream.of(values()).filter(f -> cardNumber.startsWith(f.getCardNumber()))
                .map(CardType::getClassType).findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid CardNumber!"));
    }
}
