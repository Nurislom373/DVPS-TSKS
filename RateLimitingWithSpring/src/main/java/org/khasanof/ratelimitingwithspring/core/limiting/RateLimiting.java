package org.khasanof.ratelimitingwithspring.core.limiting;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 8:12 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public interface RateLimiting {

    boolean tryRequest(int token);

    Long availableToken();

    void addTokens();

}
