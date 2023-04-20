package org.khasanof.ratelimitingwithspring.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/11/2023
 * <br/>
 * Time: 1:50 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.test
 */
@RestController
@RequestMapping(value = "/auth/*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUserService service;

    @RequestMapping(value = "getToken/{key}", method = RequestMethod.GET)
    public ResponseEntity<String> getToken(@PathVariable String key) {
        return new ResponseEntity<>(service.getToken(key), HttpStatus.OK);
    }
}
