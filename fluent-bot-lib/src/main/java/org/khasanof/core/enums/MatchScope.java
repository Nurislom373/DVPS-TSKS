package org.khasanof.core.enums;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 21:51
 * <br/>
 * Package: org.khasanof.core.enums
 */
public enum MatchScope {
    START_WITH,
    END_WITH,
    CONTAINS,
    EQUALS,
    REGEX,
    EQUALS_IGNORE_CASE,
    EXPRESSION,

    /**
     * this new feature use only message and callback handlers!
     */
    EXPRESSION_VARIABLE
}
