package org.khasanof.ratelimitingwithspring.core.strategy;

import java.io.IOException;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/13/2023
 * <br/>
 * Time: 11:08 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core
 */
public abstract class AbstractReadStrategy<T extends BaseRS> {

    public abstract List<T> read(String path) throws IOException;

}
