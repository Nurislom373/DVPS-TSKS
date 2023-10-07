package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.state.StateConfigurerAdapter;
import org.khasanof.springbootstarterfluent.core.state.configurer.StateConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.EnumSet;

@SpringBootApplication
public class SpringFluentTestApplication implements StateConfigurerAdapter<BotState> {

    public static void main(String[] args) {
        SpringApplication.run(SpringFluentTestApplication.class, args);
    }

    @Override
    public void configure(StateConfigurer<BotState> state) {
        state.initial(BotState.START)
                .states(EnumSet.allOf(BotState.class));
    }

}
