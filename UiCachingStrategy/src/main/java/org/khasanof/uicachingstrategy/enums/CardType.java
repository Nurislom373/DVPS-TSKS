package org.khasanof.uicachingstrategy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    UZCARD("8600"),
    HUMO("9860");
    private final String cardNumber;
}
