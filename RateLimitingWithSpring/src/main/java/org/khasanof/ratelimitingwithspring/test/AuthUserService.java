package org.khasanof.ratelimitingwithspring.test;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.khasanof.ratelimitingwithspring.core.utils.JWTUtils;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private final AuthUserRepository repository;

    public void create(AuthUserEntity user) {
        if (user != null) {
            repository.save(user);
        }
    }

    public String login(AuthRequestDTO dto) {
        if (dto != null) {
            Optional<AuthUserEntity> optional = repository.findByUsername(dto.getUsername());
            if (optional.isPresent()) {
                AuthUserEntity authUser = optional.get();
                if (authUser.getPassword().equals(dto.getPassword())) {
                    String encode = base64Encode(authUser);
                    authUser.setApiKey(encode);
                    repository.save(authUser);
                    return encode;
                }
            }
        }
        throw new RuntimeException();
    }

    public Map<String, Object> getToken() {
        Date expiryForRefreshToken = JWTUtils.getExpiryForRefreshToken();

        String refreshToken = JWT.create().withSubject("Nurislom")
                .withExpiresAt(expiryForRefreshToken)
                .withClaim("key", "5h489hg84")
                .sign(JWTUtils.getAlgorithm());

        return new HashMap<>() {{
           put("token", refreshToken);
        }};
    }

    private String base64Encode(AuthUserEntity user) {
        return Base64.getEncoder().encodeToString(user.getUsername().getBytes());
    }

}
