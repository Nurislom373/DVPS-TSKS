package org.khasanof.ratelimitingwithspring.core.common.read;

import org.khasanof.ratelimitingwithspring.core.config.ReadLimitsPropertiesConfig;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 5:40 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.read
 */
public interface ReadConfigAndSave {

    void readConfigAndSave(ReadLimitsPropertiesConfig propertiesConfig);

}
