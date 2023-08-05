package org.khasanof.springbootstarterfluent.core.collector.loader;

import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector
 * @since 8/4/2023 9:03 PM
 */
public interface ResourceLoader {

    Map<String, Object> getBeans();

}
