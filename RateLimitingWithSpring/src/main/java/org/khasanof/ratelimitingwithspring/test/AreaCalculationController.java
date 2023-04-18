package org.khasanof.ratelimitingwithspring.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/*")
public class AreaCalculationController {

    @GetMapping(value = "value")
    public ResponseEntity<String> value() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "check", method = RequestMethod.GET)
    public ResponseEntity<String> check() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "echo/{value}")
    public ResponseEntity<String> echo(@PathVariable String value) {
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
