package com.example.springfluenttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example"})
public class SpringFluentTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFluentTestApplication.class, args);
    }

}
