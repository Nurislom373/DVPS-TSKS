package org.khasanof.ratelimitingwithspring.core.common.load.genericLoad;

import jakarta.annotation.PostConstruct;
import org.khasanof.ratelimitingwithspring.core.factory.ReadStrategyClassFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 6:32 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.load
 */
public abstract class AbstractGenericLoadIPostConstruct<T> {

    protected final ReadStrategyClassFactory classFactory;

    protected List<T> list;

    protected boolean isPresent;

    protected AbstractGenericLoadIPostConstruct(ReadStrategyClassFactory classFactory) {
        this.classFactory = classFactory;
    }

    abstract void afterPropertiesSet(String path);

     protected void checkPathAndSetFalse(String path) {
         if (Objects.isNull(path)) {
             setList(new ArrayList<>());
             setPresent(false);
         }
     }

    public List<T> getList() {
        return list;
    }

    protected void setList(List<T> list) {
        this.list = list;
    }

    public boolean isPresent() {
        return isPresent;
    }

    protected void setPresent(boolean present) {
        isPresent = present;
    }
}
