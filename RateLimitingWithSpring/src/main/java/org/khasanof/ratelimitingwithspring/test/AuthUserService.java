package org.khasanof.ratelimitingwithspring.test;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.utils.JWTUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/11/2023
 * <br/>
 * Time: 1:51 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.test
 */
@Service
@RequiredArgsConstructor
public class AuthUserService {

    public String getToken(String key) {
        return JWT.create()
                .withSubject("khasanof")
                .withExpiresAt(JWTUtils.getExpiryForRefreshToken())
                .withIssuer("/api/v1/value")
                .withClaim("key", key)
                .withClaim("role", "ADMIN")
                .sign(JWTUtils.getAlgorithm());
    }

}
