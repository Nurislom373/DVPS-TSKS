package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.state.StateConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Set;

@SpringBootApplication
public class SpringFluentTestApplication implements StateConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringFluentTestApplication.class, args);
    }

    @Override
    public Set<Class<? extends Enum>> stateEnums() {
        return Set.of(State.class);
    }
}
