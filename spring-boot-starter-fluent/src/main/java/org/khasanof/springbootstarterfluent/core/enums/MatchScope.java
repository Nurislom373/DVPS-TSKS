package org.khasanof.springbootstarterfluent.core.enums;

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

    /**
     * this expression EvalEx is used, we can write it like spring expression language.
     *
     * <br/>
     *
     * Using Example:
     *
     * <pre>
     *    &#064HandleMessage(value = "START_WITH('a', value) && END_WITH('z', value)", scope = MatchScope.EXPRESSION)
     * </pre>
     *
     */
    EXPRESSION,

    /**
     * this new feature use only message handlers!
     *
     * <br/>
     *
     * Using Example:
     *
     * <pre>
     *    &#064;HandleMessage(value = "/name {name:[a-z]}", scope = MatchScope.VAR_EXPRESSION)
     * </pre>
     *
     * You write the name of the first variable in {} brackets, then you put ':' and you write a regular expression,
     * it checks that everything matches
     *
     */
    VAR_EXPRESSION
}
