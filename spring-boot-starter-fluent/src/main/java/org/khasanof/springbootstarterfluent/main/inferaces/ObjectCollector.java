package org.khasanof.springbootstarterfluent.main.inferaces;

import java.util.List;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.main.inferaces
 * @since 8/18/2023 5:46 AM
 */
public interface ObjectCollector<T> {

    Set<T> getAll();

    void addAll(Set<T> ts);

}
