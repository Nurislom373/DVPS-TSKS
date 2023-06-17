package org.khasanof.ratelimitingwithspring.core.common.search;

import org.khasanof.ratelimitingwithspring.core.common.search.classes.RLSearch;
import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.khasanof.ratelimitingwithspring.core.utils.BaseUtils;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author: Nurislom
 * <br/>
 * Date: 27.05.2023
 * <br/>
 * Time: 19:39
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.search
 */
public abstract class RateLimitingSearchUtil {

    protected boolean areEqual(Api api, RLSearch search) {
        System.out.println(search.getUri());
        System.out.println("api = " + api);

        if (search.getAttributes() != null && !search.getAttributes().isEmpty()) {
            if (twoURI(api.getUrl(), search.getUri())) {
                System.out.println("Enter with attributes");
                if (api.getMethod().equals(RequestMethod.resolve(search.getMethod()))) {
                    return BaseUtils.areEqual(api.getAttributes(), search.getAttributes());
                }
            }
        } else {
            System.out.println("Without Attributes");
            if (api.getUrl().equals(search.getUri())) {
                return api.getMethod().equals(RequestMethod.resolve(search.getMethod()));
            }
        }
        return false;
    }

    protected boolean twoURI(String limitURI, String requestURI) {
        return requestURI.startsWith(limitURI.replace("*", ""));
    }

}
